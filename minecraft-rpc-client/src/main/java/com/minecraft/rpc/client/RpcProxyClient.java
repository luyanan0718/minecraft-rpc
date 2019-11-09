package com.minecraft.rpc.client;

import com.minecraft.rpc.client.discovery.IServiceDiscovery;

import java.lang.reflect.Proxy;

/**
 * @author luyanan
 * @since 2019/11/9
 * <p>代理</p>
 **/
public class RpcProxyClient {


    private IServiceDiscovery serviceDiscovery;

    public RpcProxyClient(IServiceDiscovery serviceDiscovery) {
        this.serviceDiscovery = serviceDiscovery;
    }

    public <T> T clientProxy(Class<T> interfaceClazz, String version) {
        return (T) Proxy.newProxyInstance(interfaceClazz.getClassLoader(),
                new Class[]{interfaceClazz}, new RemoteInvocationHandler(serviceDiscovery, version));
    }

}
