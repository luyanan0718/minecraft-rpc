package com.minecraft.rpc.core;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * @author luyanan
 * @since 2019/11/8
 * <p>工具类</p>
 **/
public class RpcUtils {


    public static Integer getRandomPort() {
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return serverSocket.getLocalPort();
    }

    public static void main(String[] args) {
        System.out.println(getRandomPort());
    }

}
