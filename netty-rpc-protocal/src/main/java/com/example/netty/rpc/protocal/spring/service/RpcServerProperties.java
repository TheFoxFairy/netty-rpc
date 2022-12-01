package com.example.netty.rpc.protocal.spring.service;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "rpc")
public class RpcServerProperties {

    private int servicePort;
    private String serviceAddress;
}
