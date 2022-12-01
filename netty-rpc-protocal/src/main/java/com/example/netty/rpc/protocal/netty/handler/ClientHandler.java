package com.example.netty.rpc.protocal.netty.handler;

import com.example.netty.rpc.protocal.core.RequestHolder;
import com.example.netty.rpc.protocal.core.RpcFuture;
import com.example.netty.rpc.protocal.core.RpcProtocal;
import com.example.netty.rpc.protocal.core.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClientHandler extends SimpleChannelInboundHandler<RpcProtocal<RpcResponse>> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcProtocal<RpcResponse> msg) throws Exception {
        log.info("receive Rpc Server Result");
        long requestId = msg.getHeader().getRequestId();
        // 删除映射关系
        RpcFuture<RpcResponse> future = RequestHolder.REQUEST_MAP.remove(requestId);
        // 我们之前说异步等待服务端发送数据过来，那么只要服务端发送数据过来，就会调用管道 ClientHandler 的read方法
        // 那么当初future.getPromise().get()如果不再阻塞获取数据呢？就是通过给Promise中的Success设置值，同时会唤醒阻塞的线程
        // 一当唤醒线程， future.getPromise().get()就会不再阻塞，就获取到服务端返回的数据
        future.getPromise().setSuccess(msg.getContent());
    }
}
