package com.minecraft.rpc.client;

import com.minecraft.rpc.client.discovery.IServiceDiscovery;
import com.minecraft.rpc.core.RpcRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author luyanan
 * @since 2019/11/8
 * <p>jdk 代理handler</p>
 **/
@Slf4j
public class RemoteInvocationHandler implements InvocationHandler {


    private IServiceDiscovery serviceDiscovery;

    private String version;

    public RemoteInvocationHandler(IServiceDiscovery serviceDiscovery, String version) {
        this.serviceDiscovery = serviceDiscovery;
        this.version = version;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if ("toString".equals(method.getName())) {
            return method.invoke(proxy, method);
        }
        // 包装请求数据
        RpcRequest request = new RpcRequest();
        request.setClassName(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setParams(args);
        request.setParamsType(method.getParameterTypes());
        request.setVersion(version);

        String serverName = request.getClassName();
        if (!StringUtils.isEmpty(version)) {
            serverName += "-" + version;
        }
        String discovery = serviceDiscovery.discovery(serverName);

        RpcNetTransport rpcNetTransport = new RpcNetTransport(discovery);

        log.info("服务名称:{},发送请求地址:{}", serverName, discovery);
        return rpcNetTransport.send(request);
    }
}
