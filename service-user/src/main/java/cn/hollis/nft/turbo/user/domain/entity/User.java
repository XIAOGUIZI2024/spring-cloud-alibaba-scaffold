package cn.hollis.nft.turbo.user.domain.entity;

import cn.hollis.nft.turbo.api.user.constant.UserRole;
import cn.hollis.nft.turbo.api.user.constant.UserStateEnum;
import cn.hollis.nft.turbo.datasource.domain.entity.BaseEntity;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * 用户实体
 *
 * @author hollis
 */
@Setter
@Getter
@TableName("users")
public class User extends BaseEntity {

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 密码哈希
     */
    private String passwordHash;

    /**
     * 状态
     */
    private UserStateEnum state;

    /**
     * 邀请码
     */
    private String inviteCode;

    /**
     * 邀请人用户ID
     */
    private String inviterId;

    /**
     * 手机号
     */
    private String telephone;

    /**
     * 用户角色
     */
    private UserRole userRole;

    /**
     * 注册
     */
    public User register(String telephone, String nickName, String password, String inviteCode, String inviterId) {
        this.setTelephone(telephone);
        this.setNickName(nickName);
        this.setPasswordHash(DigestUtil.md5Hex(password));
        this.setState(UserStateEnum.INIT);
        this.setUserRole(UserRole.CUSTOMER);
        this.setInviteCode(inviteCode);
        this.setInviterId(inviterId);
        return this;
    }
}
