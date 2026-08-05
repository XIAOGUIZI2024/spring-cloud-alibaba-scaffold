package cn.hollis.nft.turbo.user.domain.service;

import cn.hollis.nft.turbo.api.user.constant.UserStateEnum;
import cn.hollis.nft.turbo.api.user.response.UserOperatorResponse;
import cn.hollis.nft.turbo.lock.DistributeLock;
import cn.hollis.nft.turbo.user.domain.entity.User;
import cn.hollis.nft.turbo.user.infrastructure.exception.UserErrorCode;
import cn.hollis.nft.turbo.user.infrastructure.exception.UserException;
import cn.hollis.nft.turbo.user.infrastructure.mapper.UserMapper;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static cn.hollis.nft.turbo.user.infrastructure.exception.UserErrorCode.*;

/**
 * 用户服务（简化版）
 *
 * @author hollis
 */
@Service
public class UserService extends ServiceImpl<UserMapper, User> {

    private static final String DEFAULT_NICK_NAME_PREFIX = "藏家_";

    /**
     * 注册时生成昵称/邀请码去重的最大重试次数
     */
    private static final int MAX_REGISTER_RETRY_TIMES = 5;

    @Autowired
    private UserMapper userMapper;

    /**
     * 注册
     */
    @DistributeLock(keyExpression = "#telephone", scene = "USER_REGISTER")
    @Transactional(rollbackFor = Exception.class)
    public UserOperatorResponse register(String telephone, String inviteCode) {
        if (userMapper.findByTelephone(telephone) != null) {
            throw new UserException(DUPLICATE_TELEPHONE_NUMBER);
        }

        // 生成昵称与用户自己的邀请码，用 DB 查询兜底去重（6位随机串碰撞概率极低）
        String defaultNickName;
        String randomString;
        int retryTimes = 0;
        do {
            randomString = RandomUtil.randomString(6).toUpperCase();
            //前缀 + 6位随机数 + 手机号后四位
            defaultNickName = DEFAULT_NICK_NAME_PREFIX + randomString + telephone.substring(7, 11);
            retryTimes++;
        } while (retryTimes < MAX_REGISTER_RETRY_TIMES
                && (userMapper.findByNickname(defaultNickName) != null
                || userMapper.findByInviteCode(randomString) != null));

        // 解析传入的邀请码，定位邀请人
        String inviterId = null;
        if (StrUtil.isNotBlank(inviteCode)) {
            User inviter = userMapper.findByInviteCode(inviteCode);
            if (inviter != null) {
                inviterId = inviter.getId().toString();
            }
        }

        User user = new User();
        user.register(telephone, defaultNickName, telephone, inviteCode, null);
        boolean saved = save(user);
        Assert.isTrue(saved, UserErrorCode.USER_OPERATE_FAILED.getCode());

        UserOperatorResponse response = new UserOperatorResponse();
        response.setSuccess(true);
        return response;
    }

    /**
     * 通过手机号查询
     */
    public User findByTelephone(String telephone) {
        return userMapper.findByTelephone(telephone);
    }

    /**
     * 通过ID查询
     */
    public User findById(Long userId) {
        return userMapper.findById(userId);
    }
}
