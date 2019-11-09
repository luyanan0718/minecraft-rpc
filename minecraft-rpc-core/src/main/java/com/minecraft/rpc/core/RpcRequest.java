package com.minecraft.rpc.core;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author luyanan
 * @since 2019/11/8
 * <p>请求封装类</p>
 **/
public class RpcRequest implements Serializable {


    /**
     * <p>类名</p>
     *
     * @author luyanan
     * @since 2019/11/8
     */
    private String className;


    /**
     * <p>方法名字</p>
     *
     * @author luyanan
     * @since 2019/11/8
     */
    private String methodName;


    /**
     * <p>参数</p>
     *
     * @author luyanan
     * @since 2019/11/8
     */
    private Object[] params;


    /**
     * <p>参数类型</p>
     *
     * @author luyanan
     * @since 2019/11/8
     */
    private Class<?>[] paramsType;


    /**
     * <p>版本号</p>
     *
     * @author luyanan
     * @since 2019/11/8
     */
    private String version;


    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

    public Class<?>[] getParamsType() {
        return paramsType;
    }

    public void setParamsType(Class<?>[] paramsType) {
        this.paramsType = paramsType;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
