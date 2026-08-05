package cn.hollis.nft.turbo.admin.param;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * 管理员注册/登录参数
 *
 * @author DaDagui
 */
@Setter
@Getter
public class AdminRegisterParam {

    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能为空")
    private String telephone;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;
}
