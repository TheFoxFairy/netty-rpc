package com.example.netty.rpc.protocal.core;

import lombok.Data;

import java.io.Serializable;

@Data
public class RpcRequest implements Serializable {

    private String className;// 类名
    private String methodName;// 方法名称
    private Object[] params;// 请求参数
    private Class<?>[] parameterTypes;//参数类型
}
