package com.minecraft.example.service;

import com.minecraft.rpc.annotation.RpcClient;

/**
 * @author luyanan
 * @since 2019/11/9
 * <p></p>
 **/
public interface IUserService {

    String getUser(Long id);
}
