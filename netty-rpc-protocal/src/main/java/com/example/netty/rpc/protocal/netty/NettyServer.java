package com.example.netty.rpc.protocal.netty;

import com.example.netty.rpc.protocal.code.RpcDecode;
import com.example.netty.rpc.protocal.code.RpcEncode;
import com.example.netty.rpc.protocal.netty.handler.Serverhandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyServer {

    private String serverAddress; //服务地址
    private int serverPort; //端口

    public NettyServer(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    public void startNettyServer(){
        log.info("begin start Netty server");
        EventLoopGroup boss=new NioEventLoopGroup();
        EventLoopGroup worker=new NioEventLoopGroup();

        ServerBootstrap bootstrap=new ServerBootstrap();
        bootstrap.group(boss,worker)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().
                                addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,
                                        12,
                                        4,
                                        0,
                                        0))
                                .addLast(new RpcDecode())
                                .addLast(new RpcEncode())
                                .addLast(new Serverhandler());

                    }
                });
        try {
            ChannelFuture future=bootstrap.bind(this.serverAddress,this.serverPort).sync();
            log.info("Server started Success on Port,{}",this.serverPort);
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
