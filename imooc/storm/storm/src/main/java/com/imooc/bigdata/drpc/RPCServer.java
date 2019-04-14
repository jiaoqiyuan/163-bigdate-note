package com.imooc.bigdata.drpc;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.RPC;

import java.io.IOException;

/**
 * RPC Server服务
 */
public class RPCServer {
    public static void main(String[] args) throws IOException {
        Configuration configuration = new Configuration();
        RPC.Builder builder = new RPC.Builder(configuration);
        RPC.Server server = builder.setProtocol(UserService.class)
                .setInstance(new UserServiceImpl())
                .setBindAddress("localhost")
                .setPort(9999)
                .build();
//        System.setProperty("hadoop.home.dir", "C:\\winutils");
        server.start();
    }
}
