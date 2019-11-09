package com.minecraft.rpc;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author luyanan
 * @since 2019/11/8
 * <p>配置文件</p>
 **/
@ConfigurationProperties(prefix = RpcProperties.prefix)
public class RpcProperties {
    public final static String prefix = "minecraft.rpc";


    /**
     * <p>zookeeper的连接信息</p>
     *
     * @author luyanan
     * @since 2019/11/8
     */
    private String zookeeper_connect;


    /**
     * <p>rpc 服务端的端口号</p>
     *
     * @author luyanan
     * @since 2019/11/8
     */
    private Integer serverPort;


    public String getZookeeper_connect() {
        return zookeeper_connect;
    }

    public void setZookeeper_connect(String zookeeper_connect) {
        this.zookeeper_connect = zookeeper_connect;
    }

    public Integer getServerPort() {
        return serverPort;
    }

    public void setServerPort(Integer serverPort) {
        this.serverPort = serverPort;
    }
}
