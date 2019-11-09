package com.minecraft.rpc.client.discovery;

import java.util.List;

/**
 * @author luyanan
 * @since 2019/11/8
 * <p>负载均衡的策略</p>
 **/
public interface LoadBalanceStrategy {

    /**
     * <p>从host列表中根据负载均衡获取host</p>
     *
     * @param hosts
     * @return {@link String}
     * @author luyanan
     * @since 2019/11/8
     */
    String selectHost(List<String> hosts);

}
