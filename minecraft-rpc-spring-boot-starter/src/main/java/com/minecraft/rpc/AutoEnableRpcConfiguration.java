package com.minecraft.rpc;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author luyanan
 * @since 2019/11/8
 * <p>自动注入rpcBean 的开关</p>
 **/
@Import({RpcServerConfig.class, RpcClientConfig.class})
@Configuration
public class AutoEnableRpcConfiguration {


}
