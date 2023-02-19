package com.example.codec;

import com.example.pojo.MessageProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class ClientHandler extends SimpleChannelInboundHandler<MessageProtocol> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MessageProtocol messageProtocol) throws Exception {

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for (int i = 0; i < 2; i++) {
            String msg = "你好，我是张三！" + 99*i;
            MessageProtocol messageProtocol = new MessageProtocol();
            messageProtocol.setLen(msg.getBytes(CharsetUtil.UTF_8).length);
            messageProtocol.setContent(msg.getBytes(CharsetUtil.UTF_8));
            ctx.writeAndFlush(messageProtocol);
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
