package com.example.netty.rpc.consumer.controller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {
        "com.example.netty.rpc.consumer.controller",
        "com.example.netty.rpc.protocal.spring.reference",
        "com.example.netty.rpc.protocal.annotation"
})
@SpringBootApplication
public class NettyRpcConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(NettyRpcConsumerApplication.class, args);
    }

}
