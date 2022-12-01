package com.example.netty.rpc.protocal.spring.reference;

import lombok.Data;

@Data
public class RpcClientProperties {

    private String serviceAddress;

    private int servicePort;
}
