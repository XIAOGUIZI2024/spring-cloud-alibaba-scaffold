package cn.hollis.nft.turbo.user.facade;

import cn.hollis.nft.turbo.api.user.request.UserRegisterRequest;
import cn.hollis.nft.turbo.api.user.response.UserOperatorResponse;
import cn.hollis.nft.turbo.api.user.service.UserManageFacadeService;
import cn.hollis.nft.turbo.rpc.facade.Facade;
import cn.hollis.nft.turbo.user.domain.service.UserService;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * 用户管理门面服务实现
 *
 * @author DaDagui
 */
@DubboService(version = "1.0.0")
public class UserManageFacadeServiceImpl implements UserManageFacadeService {

    @Resource
    private UserService userService;

    @Override
    @Facade
    public UserOperatorResponse registerAdmin(UserRegisterRequest request) {
        return userService.registerAdmin(request.getTelephone(), request.getPassword());
    }

    @Override
    public UserOperatorResponse freeze(Long userId) {
        return userService.freeze(userId);
    }

    @Override
    public UserOperatorResponse unfreeze(Long userId) {
        return userService.unfreeze(userId);
    }
}
