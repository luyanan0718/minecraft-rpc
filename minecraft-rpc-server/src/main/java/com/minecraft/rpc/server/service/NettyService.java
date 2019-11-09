package com.minecraft.rpc.server.service;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @author luyanan
 * @since 2019/11/8
 * <p>使用Netty 实现服务通信</p>
 **/
@Slf4j
public class NettyService implements IService {


    @Override
    public void startService(Map<String, Object> handler, Integer port) {
        log.info("启动Netty服务,port:" + port);
        // 接受客户端的连接
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        //  处理已经被接受的连接
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap()
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {

                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)))
                                    .addLast(new ObjectEncoder())
                                    .addLast(new NettyProcessHandler(handler));
                        }
                    });


            ChannelFuture future = bootstrap.bind(port).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
