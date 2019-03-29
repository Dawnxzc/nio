package com.luc.learn.bg.nio.netty.zc.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * 客户端:
 * 连接服务器、写数据。等待接收服务器的返回数据。关闭连接
 * @author xuzhichao
 * @date 2019/3/29 09:20
 * @Description:
 */
public class EchoClient {

    private final String host;

    private final int port;

    public EchoClient(String host, int port){
        this.host = host;
        this.port = port;
    }

    public void start() throws Exception {
        // EventLoopGroup 可以理解成一个线程池，用来处理连接、接收数据、发送请求
        EventLoopGroup eventExecutors = null;

        try {
            // 创建Bootstrap对象来引导启动客户端
            Bootstrap bootstrap = new Bootstrap();

            // 创建EventLoopGroup对象 并设置到Bootstrap中
            eventExecutors = new NioEventLoopGroup();

            // 创建InetSocketAddress 并设置到Bootstrap中
            InetSocketAddress inetSocketAddress = new InetSocketAddress(host, port);

            bootstrap.group(eventExecutors).channel(NioSocketChannel.class).remoteAddress(inetSocketAddress).handler(new ChannelInitializer<SocketChannel>() {

                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline().addLast(new EchoClientHandler());
                }
            });

            // 连接服务器 sync() 表示同步操作
            ChannelFuture channelFuture = bootstrap.connect().sync();
            // 释放资源
            channelFuture.channel().closeFuture().sync();
        } finally {
            eventExecutors.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws Exception {
        new EchoClient("localhost", 2111).start();
    }
}
