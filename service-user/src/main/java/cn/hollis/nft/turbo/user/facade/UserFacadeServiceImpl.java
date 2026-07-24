package cn.hollis.nft.turbo.user.facade;

import cn.hollis.nft.turbo.api.user.request.*;
import cn.hollis.nft.turbo.api.user.request.condition.UserIdQueryCondition;
import cn.hollis.nft.turbo.api.user.request.condition.UserPhoneQueryCondition;
import cn.hollis.nft.turbo.api.user.response.UserOperatorResponse;
import cn.hollis.nft.turbo.api.user.response.UserQueryResponse;
import cn.hollis.nft.turbo.api.user.response.data.UserInfo;
import cn.hollis.nft.turbo.api.user.service.UserFacadeService;
import cn.hollis.nft.turbo.base.response.PageResponse;
import cn.hollis.nft.turbo.rpc.facade.Facade;
import cn.hollis.nft.turbo.user.domain.entity.User;
import cn.hollis.nft.turbo.user.domain.entity.convertor.UserConvertor;
import cn.hollis.nft.turbo.user.domain.service.UserService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 用户门面服务实现
 *
 * @author Hollis
 */
@DubboService(version = "1.0.0")
public class UserFacadeServiceImpl implements UserFacadeService {

    @Autowired
    private UserService userService;

    @Override
    public UserQueryResponse<UserInfo> query(UserQueryRequest userQueryRequest) {
        User user = switch (userQueryRequest.getUserQueryCondition()) {
            case UserIdQueryCondition userIdQueryCondition:
                yield userService.findById(userIdQueryCondition.getUserId());
            case UserPhoneQueryCondition userPhoneQueryCondition:
                yield userService.findByTelephone(userPhoneQueryCondition.getTelephone());
            default:
                throw new UnsupportedOperationException("unsupported query condition");
        };

        UserQueryResponse<UserInfo> response = new UserQueryResponse<>();
        response.setSuccess(true);
        response.setData(UserConvertor.INSTANCE.mapToVo(user));
        return response;
    }

    @Override
    public PageResponse<UserInfo> pageQuery(UserPageQueryRequest request) {
        // 简化实现
        return PageResponse.of(null, 0, request.getPageSize(), request.getCurrentPage());
    }

    @Override
    @Facade
    public UserOperatorResponse register(UserRegisterRequest request) {
        return userService.register(request.getTelephone(), request.getInviteCode());
    }

    @Override
    @Facade
    public UserOperatorResponse modify(UserModifyRequest request) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    @Override
    @Facade
    public UserOperatorResponse auth(UserAuthRequest request) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    @Override
    @Facade
    public UserOperatorResponse active(UserActiveRequest request) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    @Override
    public UserQueryResponse<UserInfo> queryByTelephone(String telephone) {
        User user = userService.findByTelephone(telephone);
        UserQueryResponse<UserInfo> response = new UserQueryResponse<>();
        response.setSuccess(true);
        response.setData(user != null ? UserConvertor.INSTANCE.mapToVo(user) : null);
        return response;
    }
}
