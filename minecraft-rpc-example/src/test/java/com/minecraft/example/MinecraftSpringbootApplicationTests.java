package com.minecraft.example;

import com.minecraft.example.api.IUserApi;
import com.minecraft.rpc.client.RpcProxyClient;
import com.minecraft.rpc.client.discovery.IServiceDiscovery;
import com.minecraft.rpc.client.discovery.LoadBalanceStrategy;
import com.minecraft.rpc.client.discovery.RandomLoadBalance;
import com.minecraft.rpc.client.discovery.ZookeeperServiceDiscovery;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

class MinecraftSpringbootApplicationTests {

    @Test
    void contextLoads() throws Exception {


        String connectStr = "192.168.91.128:2181";
        //初始化zookeeper的连接， 会话超时时间是5s，衰减重试
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder().
                connectString(connectStr).sessionTimeoutMs(5000).
                retryPolicy(new ExponentialBackoffRetry(1000, 3)).
                namespace("registry")
                .build();
        curatorFramework.start();


        List<String> list = curatorFramework.getChildren().forPath("/com.minecraft.example.api.IUserApi-1");
        System.out.println(list);


        LoadBalanceStrategy loadBalanceStrategy = new RandomLoadBalance();
        IServiceDiscovery serviceDiscovery = new ZookeeperServiceDiscovery(curatorFramework, loadBalanceStrategy);
        RpcProxyClient rpcProxyClient = new RpcProxyClient(serviceDiscovery);
        IUserApi userApi = rpcProxyClient.clientProxy(IUserApi.class, "1");
        System.out.println(userApi.getUser(111L));


    }

}
