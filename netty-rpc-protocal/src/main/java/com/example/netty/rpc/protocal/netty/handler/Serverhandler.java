package com.example.netty.rpc.protocal.netty.handler;

import com.example.netty.rpc.protocal.constant.ReqType;
import com.example.netty.rpc.protocal.core.*;
import com.example.netty.rpc.protocal.spring.service.Mediator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class Serverhandler extends SimpleChannelInboundHandler<RpcProtocal<RpcRequest>> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcProtocal<RpcRequest> msg) throws Exception {

        RpcProtocal<RpcResponse> resProtocal = new RpcProtocal<>();
        Header header = msg.getHeader();
        header.setReqType(ReqType.RESPONSE.code());
        // 这里就是调用server方法的地方
        Object result = Mediator.getInstance().processor(msg.getContent());
        resProtocal.setHeader(header);
        RpcResponse rpcResponse = new RpcResponse();
        rpcResponse.setMsg("success");
        rpcResponse.setData(result);
        resProtocal.setContent(rpcResponse);
        ctx.writeAndFlush(resProtocal);

    }
}
