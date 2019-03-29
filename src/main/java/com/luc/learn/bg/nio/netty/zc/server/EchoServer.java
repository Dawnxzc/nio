package com.luc.learn.bg.nio.netty.zc.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 服务端：
 * 配置服务器的功能：线程、端口、实现服务处理程序、业务逻辑
 * 决定当有请求连接或接收数据时该做什么
 * @author xuzhichao
 * @date 2019/3/28 22:50
 * @Description:
 */
public class EchoServer {

    private final int port;

    private final String localAddress;

    public EchoServer(int port, String localAddress){
        this.port = port;
        this.localAddress = localAddress;
    }

    public void start() throws InterruptedException {
        EventLoopGroup eventLoopGroup = null;

        try {
            // 创建ServerBootstrap实例来引导绑定和启动服务器
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            // 创建EventLoopGroup对象来来处理事件，如接收新连接、接收数据、写数据扽
            eventLoopGroup = new NioEventLoopGroup();

            // 指定通道类型为NioServerSocketChannel，设置InetSocketAddress让服务器监听某个端口已等待客户端连接
            serverBootstrap.group(eventLoopGroup).channel(NioServerSocketChannel.class).localAddress(localAddress, port).childHandler(new ChannelInitializer<Channel>() {

                // 设置childHandler 执行所有的连接请求
                @Override
                protected void initChannel(Channel channel) throws Exception {
                    channel.pipeline().addLast(new EchoServerHandler());
                }
            });

            // 最后绑定服务器等待直到绑定完成，调用sync()方法会阻塞直到服务器完成绑定,然后服务器等待通道关闭，因为使用sync()，所以关闭操作也会被阻塞。
            ChannelFuture channelFuture = serverBootstrap.bind().sync();
            System.out.println("开始监听，端口为：" + channelFuture.channel().localAddress());

            channelFuture.channel().closeFuture().sync();

        } finally {
            eventLoopGroup.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new EchoServer(2111, "localhost").start();
    }
}
