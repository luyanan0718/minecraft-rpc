package com.minecraft.example.service;

import com.minecraft.example.api.IUserApi;
import com.minecraft.rpc.server.annotation.RpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author luyanan
 * @since 2019/11/9
 * <p></p>
 **/
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserApi userApi;

    @Override
    public String getUser(Long id) {
        return userApi.getUser(id);
    }
}
