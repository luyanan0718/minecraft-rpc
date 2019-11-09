package com.minecraft.rpc.server.registry;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;

/**
 * @author luyanan
 * @since 2019/11/8
 * <p>用zk 来实现注册中心</p>
 **/
@Slf4j
public class ZookeeperRegistryCenter implements IRegistryCenter {


    private CuratorFramework framework;


    public ZookeeperRegistryCenter(CuratorFramework framework) {
        this.framework = framework;
    }


    @Override
    public void registry(String serviceName, String serviceAddress) {

        String servicePath = "/" + serviceName;

        // 判断节点是否存在
        try {
            if (framework.checkExists().forPath(servicePath) == null) {

                //创建节点
                framework
                        .create()
                        .creatingParentsIfNeeded()
                        .withMode(CreateMode.PERSISTENT)
                        .forPath(servicePath);
            }
            String addressPath = servicePath + "/" + serviceAddress;
            framework.create()
                    .withMode(CreateMode.EPHEMERAL)
                    .forPath(addressPath);
            log.info("{}:服务注册成功", serviceName);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
