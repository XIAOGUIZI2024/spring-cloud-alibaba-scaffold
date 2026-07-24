package cn.hollis.nft.turbo.user.controller;

import lombok.Getter;
import lombok.Setter;

/**
 * 登录参数
 */
@Setter
@Getter
public class LoginParam {

    /**
     * 手机号
     */
    private String telephone;

    /**
     * 邀请码（注册时使用）
     */
    private String inviteCode;

    /**
     * 记住我
     */
    private Boolean rememberMe;
}
