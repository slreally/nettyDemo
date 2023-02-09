package com.example.client;

import com.example.handler.NettyRpcClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 1 连接服务端
 * 2 关闭资源
 * 3 提供发送消息的方法
 */
@Component
public class NettyRpcClient implements InitializingBean, DisposableBean {
    @Resource
    NettyRpcClientHandler nettyRpcClientHandler;

    ExecutorService executorService = Executors.newCachedThreadPool();
    EventLoopGroup group = null;
    Channel channel = null;

    /**
     * 1 连接服务端
     * 目的是获取channel
     * 这是bean的生命周期的初始化回调方法，在实例化、填充属性后的初始化bean时执行
     * 执行顺序：构造方法 -> postConstruct -> afterPropertiesSet -> init
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        //1.1 创建线程组
        group = new NioEventLoopGroup();
        //1.2 创建服务端启动助手
        Bootstrap bootstrap = new Bootstrap();
        try {
            //1.3 设置参数
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            //添加编解码器
                            channel.pipeline().addLast(new StringDecoder());
                            channel.pipeline().addLast(new StringEncoder());
                            //添加自定义处理类
                            channel.pipeline().addLast(nettyRpcClientHandler);
                        }
                    });
            //1.4 连接服务
            channel = bootstrap.connect("127.0.0.1", 9001).sync().channel();
        } catch (Exception e) {
            e.printStackTrace();
            if (group != null) {
                group.shutdownGracefully();
            }
            if (channel != null) {
                channel.close();
            }
        }
    }

    /**
     * 凡是继承DisposableBean接口的类，在bean被销毁之前都会执行该方法
     * @throws Exception
     */
    @Override
    public void destroy() throws Exception {
        if (group != null) {
            group.shutdownGracefully();
        }
        if (channel != null) {
            channel.close();
        }
    }

    /**
     * 消息发送
     * @param msg
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public Object send(String msg) throws ExecutionException, InterruptedException {
        nettyRpcClientHandler.setReqMsg(msg);
        Future submit = executorService.submit(nettyRpcClientHandler);
        return submit.get();
    }
}
