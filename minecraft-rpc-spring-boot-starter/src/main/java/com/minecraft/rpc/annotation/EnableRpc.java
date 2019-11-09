package com.minecraft.rpc.annotation;

import com.minecraft.rpc.AutoEnableRpcConfiguration;
import com.minecraft.rpc.RpcClientRegistry;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author luyanan
 * @since 2019/11/8
 * <p>是否开启rpc 开关</p>
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Import({AutoEnableRpcConfiguration.class, RpcClientRegistry.class})
public @interface EnableRpc {


    String[] value() default {};

    String[] basePackages() default {};

    Class<?>[] basePackageClasses() default {};

    Class<?>[] clients() default {};
}
