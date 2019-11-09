package com.minecraft.example.api;

import com.minecraft.rpc.annotation.RpcClient;

/**
 * @author luyanan
 * @since 2019/11/9
 * <p>接口类</p>
 **/
@RpcClient(version = "1")
public interface IUserApi {

    String getUser(Long id);

}
