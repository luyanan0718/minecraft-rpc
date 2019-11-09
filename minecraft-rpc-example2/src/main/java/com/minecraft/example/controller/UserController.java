package com.minecraft.example.controller;

import com.minecraft.example.api.IUserApi;
import com.minecraft.example.service.IUserService;
import com.minecraft.rpc.client.RpcProxyClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author luyanan
 * @since 2019/11/9
 * <p></p>
 **/
@RestController
@RequestMapping("user")
public class UserController {


    @Autowired
    private IUserService userService;

    @Autowired
    RpcProxyClient proxyClient;


    @GetMapping("user/{id}")
    public String user(@PathVariable("id") Long id) {
        String str = null;
        for (int i = 0; i < 100; i++) {
            str = userService.getUser((long) i);
            System.err.println(str);
        }


        return str;
    }


    @GetMapping("user2/{id}")
    public String user2(@PathVariable("id") Long id) {
        IUserApi userApi = proxyClient.clientProxy(IUserApi.class, "1");
        return userApi.getUser(id);
    }

}
