package com.minecraft.rpc.server.registry;

/**
 * @author luyanan
 * @since 2019/11/8
 * <p>注册中心</p>
 **/
public interface IRegistryCenter {

    /**
     * <p>服务注册名称和服务注册地址的管理</p>
     *
     * @param serviceName
     * @param serviceAddress
     * @return {@link }
     * @author luyanan
     * @since 2019/11/8
     */
    void registry(String serviceName, String serviceAddress);

}
