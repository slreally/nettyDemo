package com.example.handler;

import com.example.pojo.MessageProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class ServerHandler extends SimpleChannelInboundHandler<MessageProtocol> {
    private int count = 0;

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MessageProtocol msg) throws Exception {
        System.out.println("===服务端收到消息===");
        System.out.println("长度 " + msg.getLen());
        System.out.println("内容 " + new String(msg.getContent(), CharsetUtil.UTF_8));
        System.out.println("服务端收到的消息总数量 " + (++count));
    }
}
