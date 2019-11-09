package com.minecraft.example;

import com.minecraft.rpc.annotation.EnableRpc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.EnableMBeanExport;

@EnableRpc
@SpringBootApplication
public class MinecraftSpringbootApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(MinecraftSpringbootApplication.class, args);
//        for (String name : applicationContext.getBeanDefinitionNames()) {
//            System.out.println(name);
//        }
    }

}
