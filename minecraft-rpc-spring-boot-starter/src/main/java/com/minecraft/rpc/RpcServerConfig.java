package com.minecraft.rpc;

import com.minecraft.rpc.server.RpcServer;
import com.minecraft.rpc.server.registry.IRegistryCenter;
import com.minecraft.rpc.server.registry.ZookeeperRegistryCenter;
import com.minecraft.rpc.server.service.IService;
import com.minecraft.rpc.server.service.NettyService;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * @author luyanan
 * @since 2019/11/8
 * <p>配置中心</p>
 **/

@EnableConfigurationProperties(RpcProperties.class)
@Configuration
@ComponentScan("com.minecraft")
public class RpcServerConfig {

    // 当有zk的类的时候, 使用zk 实现注册中心
    @ConditionalOnClass(name = "org.apache.curator.framework.CuratorFramework")
    @Bean
    @Scope("prototype")
    @ConditionalOnMissingBean(CuratorFramework.class)
    public CuratorFramework curatorFramework() {
        String connectStr = "192.168.91.128:2181";
        //初始化zookeeper的连接， 会话超时时间是5s，衰减重试
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder().
                connectString(connectStr).sessionTimeoutMs(5000).
                retryPolicy(new ExponentialBackoffRetry(1000, 3)).
                namespace("registry")
                .build();
        curatorFramework.start();
        return curatorFramework;
    }


    // 当有zk的类的时候, 使用zk 实现注册中心
    @ConditionalOnBean(CuratorFramework.class)
    @Bean
    public IRegistryCenter registryCenter(CuratorFramework framework) {
        return new ZookeeperRegistryCenter(framework);
    }

    // 当有netty的类的时候, 使用netty 实现注册中心
    @ConditionalOnClass(name = "io.netty.bootstrap.ServerBootstrap")
    @Bean
    public IService service() {
        return new NettyService();
    }

    @Bean
    public RpcServer rpcServer(IRegistryCenter registryCenter, IService service, RpcProperties rpcProperties) {
        return new RpcServer(registryCenter, rpcProperties.getServerPort(), service);
    }


}
