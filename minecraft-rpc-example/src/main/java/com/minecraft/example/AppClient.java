package com.minecraft.example;

import com.minecraft.example.api.IUserApi;
import com.minecraft.rpc.RpcClientConfig;
import com.minecraft.rpc.client.RpcProxyClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author luyanan
 * @since 2019/11/9
 * <p></p>
 **/
public class AppClient {


    public static void main(String[] args) throws InterruptedException {
        ApplicationContext context = new
                AnnotationConfigApplicationContext(RpcClientConfig.class);
        RpcProxyClient rpcProxyClient = context.getBean(RpcProxyClient.class);

        IUserApi iHelloService = rpcProxyClient.clientProxy
                (IUserApi.class, "1");
        for (int i = 0; i < 10000; i++) {
            Thread.sleep(20);
            System.err.println(iHelloService.getUser(1L));
        }
    }
}
