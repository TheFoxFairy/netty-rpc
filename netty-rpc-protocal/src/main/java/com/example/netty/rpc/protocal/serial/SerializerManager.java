package com.example.netty.rpc.protocal.serial;

import java.util.concurrent.ConcurrentHashMap;

public class SerializerManager {

    private final static ConcurrentHashMap<Byte,ISerializer> serializerMap = new ConcurrentHashMap();

    static {

        ISerializer json = new JsonSerializer();
        ISerializer java = new JavaSerializer();
        serializerMap.put(json.getType(),json);
        serializerMap.put(java.getType(),java);
    }

    public static ISerializer getSerializer(byte key){
        ISerializer iSerializer = serializerMap.get(key);
        if (iSerializer ==null){
            return new JavaSerializer();
        }
        return iSerializer;

    }
}
