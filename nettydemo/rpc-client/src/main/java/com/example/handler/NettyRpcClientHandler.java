package com.example.handler;

import com.example.client.NettyRpcClient;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.Callable;

@Component
public class NettyRpcClientHandler extends SimpleChannelInboundHandler<String> implements Callable {
    ChannelHandlerContext context;

    private String reqMsg;//发送消息
    private String respMsg;//接收消息

    public void setReqMsg(String reqMsg) {
        this.reqMsg = reqMsg;
    }

    /**
     * 通道连接就绪事件
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.context = ctx;
    }

    /**
     * 通道读取就绪事件 - 读取服务端消息
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected synchronized void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        respMsg = msg;
        //唤醒等待线程
        notify();
    }

    /**
     * 给服务端发送消息
     * @return
     * @throws Exception
     */
    @Override
    public synchronized Object call() throws Exception {
        context.writeAndFlush(reqMsg);
        wait();
        return respMsg;

    }

}
