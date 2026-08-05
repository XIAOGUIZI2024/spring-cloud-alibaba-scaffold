package cn.hollis.nft.turbo.auth.controller;

import jakarta.validation.constraints.NotBlank;
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
    @NotBlank(message = "手机号不能为空")
    private String telephone;

    /**
     * 验证码
     */
    @NotBlank(message = "验证码不能为空")
    private String captcha;

    /**
     * 邀请码（注册时使用）
     */
    private String inviteCode;

    /**
     * 记住我
     */
    private Boolean rememberMe;
}
