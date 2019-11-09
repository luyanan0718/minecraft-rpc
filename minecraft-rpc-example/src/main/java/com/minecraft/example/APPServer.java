package com.minecraft.example;

import com.minecraft.rpc.RpcServerConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author luyanan
 * @since 2019/11/9
 * <p>服务端</p>
 **/
public class APPServer {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(RpcServerConfig.class);
        ((AnnotationConfigApplicationContext) context).start();
    }

}
