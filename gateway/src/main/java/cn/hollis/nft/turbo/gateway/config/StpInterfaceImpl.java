package cn.hollis.nft.turbo.gateway.config;

import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 权限与角色数据源
 * <p>
 * Sa-Token 鉴权时自动调用这两个方法获取用户的权限列表和角色列表。<br>
 * 你需要根据自己业务填充具体逻辑。
 */
@Slf4j
@Component
public class StpInterfaceImpl implements StpInterface {

    /**
     * 获取用户权限列表
     * <p>
     * 示例返回值: ["goods:read", "goods:write", "order:read"]
     * <p>
     * TODO: 改为你自己的逻辑，比如从 Session 或数据库查
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        // 示例：从 Session 取权限（需要登录时写入）
        List<String> permissions = new ArrayList<>();

        // TODO: 替换为你的权限逻辑
        // 例如:
        // UserInfo userInfo = (UserInfo) StpUtil.getSessionByLoginId(loginId).get(loginId.toString());
        // if (userInfo.getRole() == Role.ADMIN) {
        //     permissions.add("*");  // 管理员拥有所有权限
        // } else {
        //     permissions.add("user:read");
        // }

        log.debug("用户 {} 的权限列表: {}", loginId, permissions);
        return permissions;
    }

    /**
     * 获取用户角色列表
     * <p>
     * 示例返回值: ["ADMIN"] 或 ["CUSTOMER"]
     * <p>
     * TODO: 改为你自己的角色逻辑
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        // 示例：从 Session 取角色（需要登录时写入）
        List<String> roles = new ArrayList<>();

        // TODO: 替换为你的角色逻辑
        // 例如:
        // UserInfo userInfo = (UserInfo) StpUtil.getSessionByLoginId(loginId).get(loginId.toString());
        // roles.add(userInfo.getRole().name());

        log.debug("用户 {} 的角色列表: {}", loginId, roles);
        return roles;
    }
}
