package com.minecraft.rpc.server.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author luyanan
 * @since 2019/11/8
 * <p>Rpc 服务注解,会将标注了此注解的接口发布</p>
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface RpcService {


    /**
     * <p>服务的接口</p>
     *
     * @return {@link Class<?>}
     * @author luyanan
     * @since 2019/11/8
     */
    Class<?> value() default Void.class;


    /**
     * <p>版本号</p>
     *
     * @return {@link String}
     * @author luyanan
     * @since 2019/11/8
     */
    String version() default "";

}
