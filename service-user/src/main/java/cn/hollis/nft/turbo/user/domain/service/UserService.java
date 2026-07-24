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

        String defaultNickName = DEFAULT_NICK_NAME_PREFIX + RandomUtil.randomString(6).toUpperCase()
                + telephone.substring(7, 11);

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
