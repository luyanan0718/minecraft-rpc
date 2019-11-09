package com.minecraft.rpc.server.service;

import java.util.Map;

/**
 * @author luyanan
 * @since 2019/11/8
 * <p>服务</p>
 **/
public interface IService {


    /**
     * <p>启动服务</p>
     *
     * @param handler
     * @return {@link }
     * @author luyanan
     * @since 2019/11/8
     */
    void startService(Map<String, Object> handler, Integer port);
}
