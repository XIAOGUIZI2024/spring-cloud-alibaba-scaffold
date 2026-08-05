package cn.hollis.nft.turbo.user.domain.entity;

import cn.hollis.nft.turbo.api.user.constant.UserRole;
import cn.hollis.nft.turbo.api.user.constant.UserStateEnum;
import cn.hollis.nft.turbo.datasource.domain.entity.BaseEntity;
import cn.hollis.nft.turbo.user.infrastructure.mapper.AesEncryptTypeHandler;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

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
     * 最后登录时间
     */
    private Date lastLoginTime;

    /**
     * 头像地址
     */
    private String profilePhotoUrl;

    /**
     * 区块链地址
     */
    private String blockChainUrl;

    /**
     * 区块链平台
     */
    private String blockChainPlatform;

    /**
     * 实名认证
     */
    private Boolean certification;

    /**
     * 真实姓名（AES加密存储）
     */
    @TableField(typeHandler = AesEncryptTypeHandler.class)
    private String realName;

    /**
     * 身份证号（AES加密存储）
     */
    @TableField(typeHandler = AesEncryptTypeHandler.class)
    private String idCardNo;

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

    /**
     * 管理员注册
     */
    public User registerAdmin(String telephone, String nickName, String password) {
        this.setTelephone(telephone);
        this.setNickName(nickName);
        this.setPasswordHash(DigestUtil.md5Hex(password));
        this.setState(UserStateEnum.ACTIVE);
        this.setUserRole(UserRole.ADMIN);
        return this;
    }

    /**
     * 实名认证
     */
    public User auth(String realName, String idCard) {
        this.setRealName(realName);
        this.setIdCardNo(idCard);
        this.setCertification(true);
        this.setState(UserStateEnum.AUTH);
        return this;
    }

    /**
     * 激活（上链成功或状态流转）
     */
    public User active(String blockChainUrl, String blockChainPlatform) {
        this.setBlockChainUrl(blockChainUrl);
        this.setBlockChainPlatform(blockChainPlatform);
        this.setState(UserStateEnum.ACTIVE);
        return this;
    }

    /**
     * 是否可修改信息
     */
    public boolean canModifyInfo() {
        return state == UserStateEnum.INIT || state == UserStateEnum.AUTH || state == UserStateEnum.ACTIVE;
    }
}
