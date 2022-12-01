package com.example.netty.rpc.protocal.core;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class Header implements Serializable {

    private short magic;// 魔数  2个字节
    private byte serialType;// 序列化类型
    private byte reqType;// 请求类型
    private long requestId;// 请求id  8个字节
    private int length;// 消息体长度 4个字节

    public Header(){

    }

}
