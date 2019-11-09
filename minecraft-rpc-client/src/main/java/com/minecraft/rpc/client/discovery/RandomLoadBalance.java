package com.minecraft.rpc.client.discovery;

import java.util.List;
import java.util.Random;

/**
 * @author luyanan
 * @since 2019/11/8
 * <p>随机负载均衡</p>
 **/
public class RandomLoadBalance extends AbstractLoadBalance {


    @Override
    protected String doSelectHost(List<String> hosts) {
        Random random = new Random();
        return hosts.get(random.nextInt(hosts.size()));
    }
}
