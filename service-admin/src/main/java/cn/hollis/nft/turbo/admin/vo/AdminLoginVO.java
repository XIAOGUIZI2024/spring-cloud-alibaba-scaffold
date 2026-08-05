package cn.hollis.nft.turbo.admin.vo;

import cn.dev33.satoken.stp.StpUtil;
import cn.hollis.nft.turbo.api.user.response.data.UserInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 管理员登录返回
 *
 * @author DaDagui
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class AdminLoginVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 访问令牌
     */
    private String token;

    /**
     * 令牌过期时间
     */
    private Long tokenExpiration;

    public AdminLoginVO(UserInfo userInfo) {
        this.userId = userInfo.getUserId().toString();
        this.token = StpUtil.getTokenValue();
        this.tokenExpiration = StpUtil.getTokenSessionTimeout();
    }
}
