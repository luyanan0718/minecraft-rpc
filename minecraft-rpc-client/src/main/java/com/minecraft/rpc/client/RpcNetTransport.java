package com.minecraft.rpc.client;

import com.minecraft.rpc.core.RpcRequest;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.interceptor.CacheResolver;

/**
 * @author luyanan
 * @since 2019/11/8
 * <p>数据发送</p>
 **/
@Slf4j
public class RpcNetTransport extends SimpleChannelInboundHandler<Object> {

    private String serviceAdress;

    private Object result;

    public RpcNetTransport(String serviceAdress) {
        this.serviceAdress = serviceAdress;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        this.result = o;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("异常");
        ctx.close();
    }


    /**
     * <p>发送请求</p>
     *
     * @param request
     * @return {@link Object}
     * @author luyanan
     * @since 2019/11/8
     */
    public Object send(RpcRequest request) {

        // 建立客户端
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {

                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)))
                                    .addLast(new ObjectEncoder())
                                    .addLast(RpcNetTransport.this);
                        }
                    }).option(ChannelOption.TCP_NODELAY, true);

            String[] urls = serviceAdress.split(":");


            ChannelFuture future = bootstrap.connect(urls[0], Integer.parseInt(urls[1])).sync();
            future.channel().writeAndFlush(request).sync();

            if (request != null) {

                future.channel().closeFuture().sync();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            eventLoopGroup.shutdownGracefully();
        }
        return result;
    }
}
