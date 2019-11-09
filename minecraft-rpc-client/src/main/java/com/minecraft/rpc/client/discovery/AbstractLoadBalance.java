package com.minecraft.rpc.client.discovery;

import java.util.List;

/**
 * @author luyanan
 * @since 2019/11/8
 * <p>负载均衡策略抽象类</p>
 **/
public abstract class AbstractLoadBalance implements LoadBalanceStrategy {


    @Override
    public String selectHost(List<String> hosts) {
        if (null == hosts || hosts.size() == 0) {
            return null;
        }
        if (hosts.size() == 1) {
            return hosts.get(0);
        }
        return doSelectHost(hosts);
    }

    protected abstract String doSelectHost(List<String> hosts);
}
