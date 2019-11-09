package com.minecraft.rpc.annotation;

import java.lang.annotation.*;

/**
 * @author luyanan
 * @since 2019/11/9
 * <p>client注册, 标注此接口是rpc 客户端</p>
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface RpcClient {
    boolean primary() default true;

    String version() default "";
}
