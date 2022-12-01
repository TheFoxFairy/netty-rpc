package com.example.netty.rpc.protocal.serial;

import com.alibaba.fastjson.JSON;
import com.example.netty.rpc.protocal.constant.SerialType;

public class JsonSerializer  implements ISerializer{
    @Override
    public <T> byte[] serialize(T obj) {
        return JSON.toJSONString(obj).getBytes();
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) {
        return JSON.parseObject(new String(data),clazz);
    }

    @Override
    public byte getType() {
        return SerialType.JSON_SERIAL.code();
    }
}
