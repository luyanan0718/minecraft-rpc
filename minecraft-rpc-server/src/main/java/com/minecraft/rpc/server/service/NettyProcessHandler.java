package com.minecraft.rpc.server.service;

import com.minecraft.rpc.core.RpcRequest;
import com.sun.org.apache.regexp.internal.RE;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;

/**
 * @author luyanan
 * @since 2019/11/8
 * <p></p>
 **/
public class NettyProcessHandler extends SimpleChannelInboundHandler<RpcRequest> {
    private Map<String, Object> handler;

    public NettyProcessHandler(Map<String, Object> handler) {
        this.handler = handler;
    }


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcRequest rpcRequest) throws Exception {
        // 反射调用
        Object result = invoke(rpcRequest);
        channelHandlerContext.writeAndFlush(result).addListener(ChannelFutureListener.CLOSE);
    }

    private Object invoke(RpcRequest request) throws NoSuchMethodException, ClassNotFoundException, InvocationTargetException, IllegalAccessException {


        String serviceName = request.getClassName();
        //  版本号
        if (!StringUtils.isEmpty(request.getVersion())) {

            serviceName += "-" + request.getVersion();
        }
        Object service = handler.get(serviceName);

        if (null == service) {
            throw new NullPointerException(serviceName + " not found");
        }
        // 利用反射调用方法
        Class<?> clazz = Class.forName(request.getClassName());
        Method method = clazz.getMethod(request.getMethodName(), request.getParamsType());
        return method.invoke(service, request.getParams());
    }


}
