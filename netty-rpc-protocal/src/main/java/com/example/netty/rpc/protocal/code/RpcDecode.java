package com.example.netty.rpc.protocal.code;


import com.example.netty.rpc.protocal.constant.ReqType;
import com.example.netty.rpc.protocal.constant.RpcConstant;
import com.example.netty.rpc.protocal.core.*;
import com.example.netty.rpc.protocal.serial.ISerializer;
import com.example.netty.rpc.protocal.serial.SerializerManager;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class RpcDecode extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        // 如果比头的长度都小，是不是就没有内容
        if(in.readableBytes() < RpcConstant.HEAD_TOTAL_LEN){
            return;
        }
        in.markReaderIndex();
        short magic = in.readShort();

        if(magic != RpcConstant.MAGIC){
            throw  new IllegalArgumentException("....");
        }

        byte serialType = in.readByte();
        byte reqType = in.readByte();
        long requestId = in.readLong();
        int dataLength = in.readInt();
        // 如果剩余读取的字节小于dataLength
        if(in.readableBytes() < dataLength){
            in.resetReaderIndex();
            return;
        }

        byte[] content = new byte[dataLength];
        in.readBytes(content);


        Header header = new Header(magic,serialType,reqType,requestId,dataLength);

        ISerializer serializer = SerializerManager.getSerializer(serialType);

        ReqType rt = ReqType.findByCode(reqType);

        switch (rt){
            case REQUEST:
                // 反序列化
                RpcRequest rpcRequest = serializer.deserialize(content,RpcRequest.class);
                // 封装成RpcProtocal对象
                RpcProtocal<RpcRequest> rpcProtocal = new RpcProtocal<>();
                rpcProtocal.setHeader(header);
                rpcProtocal.setContent(rpcRequest);
                out.add(rpcProtocal);
                break;
            case RESPONSE:
                // 反序列化
                RpcResponse rpcResponse = serializer.deserialize(content,RpcResponse.class);
                // 封装成RpcProtocal对象
                RpcProtocal<RpcResponse> protocal = new RpcProtocal<>();
                protocal.setHeader(header);
                protocal.setContent(rpcResponse);
                out.add(protocal);
                break;
            default:
                break;
        }
    }
}
