package cn.hollis.nft.turbo.admin.param;

import lombok.Getter;
import lombok.Setter;

/**
 * 管理员登录参数
 *
 * @author DaDagui
 */
@Setter
@Getter
public class AdminLoginParam extends AdminRegisterParam {

    /**
     * 记住我
     */
    private Boolean rememberMe;
}
