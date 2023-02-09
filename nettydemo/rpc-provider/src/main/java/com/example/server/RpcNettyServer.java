package com.example.server;

import com.example.handler.RpcServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Netty服务端
 * 启动服务端 监听端口
 */
@Component
public class RpcNettyServer implements DisposableBean {

    EventLoopGroup bossGroup = null;
    EventLoopGroup workerGroup = null;

    @Resource
    private RpcServerHandler rpcServerHandler;

    public void start(String host, int port) {
        //1.创建bossGroup和workerGroup
        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup();
        try {
            //2.创建启动助手
            ServerBootstrap bootstrap = new ServerBootstrap();
            //3.设置启动参数
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            //添加String编解码器，服务端与客户端之间采用String数据格式传输
                            ch.pipeline().addLast(new StringEncoder());
                            ch.pipeline().addLast(new StringDecoder());
                            //添加自定义处理器
                            ch.pipeline().addLast(rpcServerHandler);
                        }
                    });
            //4.绑定ip和端口号
            ChannelFuture channelFuture = bootstrap.bind(host, port).sync();
            System.out.println("=========netty服务端启动成功=========");
            //5.监听通道的关闭状态
            channelFuture.channel().closeFuture();
        } catch (Exception e) {
            //关闭资源
            if (bossGroup != null) {
                bossGroup.shutdownGracefully();
            }
            if (workerGroup != null) {
                workerGroup.shutdownGracefully();
            }
        }
    }

    /**
     * 当Spring容器关闭后，执行destroy方法，关闭资源
     * @throws Exception
     */
    @Override
    public void destroy() throws Exception {
        if (bossGroup != null) {
            bossGroup.shutdownGracefully();
        }
        if (workerGroup != null) {
            workerGroup.shutdownGracefully();
        }
    }
}
