package com.minecraft.example.api;

import com.minecraft.rpc.server.annotation.RpcService;

/**
 * @author luyanan
 * @since 2019/11/9
 * <p>接口实现</p>
 **/
@RpcService(version = "1", value = IUserApi.class)
public class UserApiImpl implements IUserApi {


    @Override
    public String getUser(Long id) {
        return "user:" + id;
    }
}
