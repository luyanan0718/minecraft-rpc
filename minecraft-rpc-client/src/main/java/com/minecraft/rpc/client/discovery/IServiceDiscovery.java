package com.minecraft.rpc.client.discovery;

/**
 * @author luyanan
 * @since 2019/11/8
 * <p>服务获取接口类</p>
 **/
public interface IServiceDiscovery {

    /**
     * <p>根据服务名获取服务地址</p>
     *
     * @param serviceName
     * @return {@link String}
     * @author luyanan
     * @since 2019/11/8
     */
    String discovery(String serviceName);

}
