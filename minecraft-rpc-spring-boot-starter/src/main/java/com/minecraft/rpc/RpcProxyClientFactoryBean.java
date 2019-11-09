package com.minecraft.rpc;

import com.minecraft.rpc.client.RpcProxyClient;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author luyanan
 * @since 2019/11/9
 * <p></p>
 **/
public class RpcProxyClientFactoryBean implements FactoryBean<Object>, InitializingBean, ApplicationContextAware {

    private ApplicationContext applicationContext;


    private Class interfaceClazz;
    private String version;


    public Class getInterfaceClazz() {
        return interfaceClazz;
    }

    public void setInterfaceClazz(Class interfaceClazz) {
        this.interfaceClazz = interfaceClazz;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public Object getObject() throws Exception {
        RpcProxyClient proxyClient = applicationContext.getBean(RpcProxyClient.class);
        return proxyClient.clientProxy(interfaceClazz, version);
    }

    @Override
    public Class<?> getObjectType() {
        return this.interfaceClazz;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        this.applicationContext = applicationContext;
    }
}
