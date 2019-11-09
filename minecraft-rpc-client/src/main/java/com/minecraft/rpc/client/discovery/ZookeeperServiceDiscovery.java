package com.minecraft.rpc.client.discovery;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.ArrayList;
import java.util.List;

/**
 * @author luyanan
 * @since 2019/11/8
 * <p>使用zk 实现服务发现</p>
 **/
@Slf4j
public class ZookeeperServiceDiscovery implements IServiceDiscovery {


    private CuratorFramework framework;

    private LoadBalanceStrategy loadBalanceStrategy;

    /**
     * <p>服务地址的本地缓存</p>
     *
     * @author luyanan
     * @since 2019/11/8
     */
    private List<String> serverReps = new ArrayList<>();


    public ZookeeperServiceDiscovery(CuratorFramework framework, LoadBalanceStrategy loadBalanceStrategy) {
        this.framework = framework;
        this.loadBalanceStrategy = loadBalanceStrategy;
    }


    @Override
    public String discovery(String serviceName) {

        String servicePath = "/" + serviceName;

        try {
            if (serverReps.isEmpty()) {
                serverReps = framework.getChildren().forPath(servicePath);
                registryWatcher(servicePath);
            }
            return loadBalanceStrategy.selectHost(serverReps);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * <p>注册监听</p>
     *
     * @param servicePath
     * @return {@link }
     * @author luyanan
     * @since 2019/11/8
     */
    private void registryWatcher(String servicePath) throws Exception {

        PathChildrenCache pathChildrenCache = new PathChildrenCache(framework, servicePath, true);
        PathChildrenCacheListener listener = new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
                log.info("客户端收到节点变更的事件");
                // 再次更新本地缓存
                serverReps = curatorFramework.getChildren().forPath(servicePath);
            }
        };
        pathChildrenCache.getListenable().addListener(listener);
        pathChildrenCache.start();
    }
}
