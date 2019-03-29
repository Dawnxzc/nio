package com.luc.learn.bg.nio.netty.zc.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 客户端处理类
 * @author xuzhichao
 * @date 2019/3/29 09:38
 * @Description:
 */
public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    /**
     * 客户端连接服务器后被调用
     * @param channelHandlerContext
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext) throws Exception {
        System.out.println("客户端连接服务器，开始发送数据。。。");
        byte[] request = "QUERY TIME".getBytes();
        ByteBuf firstMessage = Unpooled.buffer(request.length);
        firstMessage.writeBytes(request);
        channelHandlerContext.writeAndFlush(firstMessage);
    }

    /**
     * 读取客户端数据
     * @param channelHandlerContext
     * @param byteBuf
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
        System.out.println("client 读取 server 端的数据。。。");
        // 服务端返回消息后
        byte[] request = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(request);
        String body = new String(request, "UTF-8");
        System.out.println("从server端接收的数据为：" + body);
    }

    /**
     * 发生异常时调用
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("客户端发生异常");
        cause.printStackTrace();
        ctx.close();
    }
}
