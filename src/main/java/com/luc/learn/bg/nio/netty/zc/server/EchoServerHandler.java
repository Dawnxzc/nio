package com.luc.learn.bg.nio.netty.zc.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Date;

/**
 * 数据写入处理器
 * @author xuzhichao
 * @date 2019/3/28 23:00
 * @Description:
 */
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 数据读取
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("server 正在读取数据.....");

        // 读取数据
        ByteBuf byteBuf = (ByteBuf) msg;
        byte[] req = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(req);
        String body = new String(req, "UTF-8");
        System.out.println("接收客户端数据：" + body);

        // 向客户端写数据
        System.out.println("server 向 client 写数据....");
        String currentTime = new Date(System.currentTimeMillis()).toString();
        ByteBuf response = Unpooled.copiedBuffer(currentTime.getBytes());
        ctx.write(response);
    }

    /**
     * 数据读取完成
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端数据读取完成");
        // 刷新后才会将数据发出到SocketChannel
        ctx.flush();
    }

    /**
     * 捕获到异常时
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 出现异常关闭channel
        cause.printStackTrace();
        ctx.close();
    }
}
