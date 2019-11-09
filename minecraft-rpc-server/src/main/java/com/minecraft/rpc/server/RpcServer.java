package com.minecraft.rpc.server;

import com.minecraft.rpc.server.annotation.RpcService;
import com.minecraft.rpc.server.registry.IRegistryCenter;
import com.minecraft.rpc.server.service.IService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.StringUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author luyanan
 * @since 2019/11/8
 * <p></p>
 **/
public class RpcServer implements ApplicationContextAware, InitializingBean {


    private IRegistryCenter registryCenter;


    private Integer port;


    private IService service;

    public RpcServer(IRegistryCenter registryCenter, Integer port, IService service) {
        this.registryCenter = registryCenter;
        this.port = port;
        this.service = service;
    }

    private Map<String, Object> handler = new HashMap<>();

    @Override
    public void afterPropertiesSet() throws Exception {

        //  启动服务
        service.startService(handler, this.port);

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {


        Map<String, Object> serviceBeanMap = applicationContext.getBeansWithAnnotation(RpcService.class);
        if (!serviceBeanMap.isEmpty()) {

            for (Object serviceBean : serviceBeanMap.values()) {
                // 拿到注解
                RpcService rpcService = serviceBean.getClass().getAnnotation(RpcService.class);
                String serviceName = "";
                if (!(rpcService.value() == Void.class)) {
                    serviceName = rpcService.value().getName();
                } else {
                    // 接口的名称
                    serviceName = serviceBean.getClass().getInterfaces()[0].getName();
                }
                String version = rpcService.version();
                if (!StringUtils.isEmpty(version)) {
                    serviceName += "-" + version;
                }
                putAndCheck(serviceName, serviceBean);
            }
        }
        // 注册
        for (Map.Entry<String, Object> entry : handler.entrySet()) {
            registryCenter.registry(entry.getKey(), getAdress() + ":" + port);
        }
    }


    /**
     * <p>检查serviceName 和版本不能重复</p>
     *
     * @param serviceName
     * @param serviceBean
     * @return {@link }
     * @author luyanan
     * @since 2019/11/8
     */
    private void putAndCheck(String serviceName, Object serviceBean) {

        if (handler.containsKey(serviceName)) {
            throw new IllegalArgumentException(serviceName + " already exists");
        }
        handler.put(serviceName, serviceBean);
    }

    /**
     * <p>获取本机的ip地址</p>
     *
     * @return {@link String}
     * @author luyanan
     * @since 2019/11/8
     */
    private static String getAdress() {
        InetAddress address = null;
        try {
            address = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return address.getHostAddress();
    }
}
