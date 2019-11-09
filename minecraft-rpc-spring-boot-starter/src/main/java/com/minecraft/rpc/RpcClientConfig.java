package com.minecraft.rpc;

import com.minecraft.rpc.client.RpcProxyClient;
import com.minecraft.rpc.client.discovery.IServiceDiscovery;
import com.minecraft.rpc.client.discovery.LoadBalanceStrategy;
import com.minecraft.rpc.client.discovery.RandomLoadBalance;
import com.minecraft.rpc.client.discovery.ZookeeperServiceDiscovery;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author luyanan
 * @since 2019/11/9
 * <p>rpc 客户端配置</p>
 **/
@Configuration
public class RpcClientConfig {


//    @ConditionalOnMissingBean(CuratorFramework.class)
//    @Bean
//    public CuratorFramework curatorFramework() {
//        String connectStr = "192.168.91.128:2181";
//        //初始化zookeeper的连接， 会话超时时间是5s，衰减重试
//        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder().
//                connectString(connectStr).sessionTimeoutMs(5000).
//                retryPolicy(new ExponentialBackoffRetry(1000, 3)).
//                namespace("registry")
//                .build();
//        curatorFramework.start();
//        return curatorFramework;
//    }

    @Bean
    public LoadBalanceStrategy loadBalanceStrategy() {
        return new RandomLoadBalance();
    }

    @Bean
    public IServiceDiscovery serviceDiscovery(CuratorFramework curatorFramework, LoadBalanceStrategy loadBalanceStrategy) {
        return new ZookeeperServiceDiscovery(curatorFramework, loadBalanceStrategy);
    }

    @Bean
    public RpcProxyClient rpcProxyClient(IServiceDiscovery serviceDiscovery) {
        return new RpcProxyClient(serviceDiscovery);
    }

}
