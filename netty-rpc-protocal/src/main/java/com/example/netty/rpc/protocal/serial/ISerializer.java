package com.example.netty.rpc.protocal.serial;

public interface ISerializer {

    /*
     * 序列化
     */
    <T> byte[] serialize(T obj);

    /**
     * 反序列化
     */
    <T> T deserialize(byte[] data,Class<T> clazz);

    /**
     * 序列化的类型
     */
    byte getType();
}
