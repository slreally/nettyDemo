package com.example.handler;

import com.alibaba.fastjson.JSON;
import com.example.anno.RpcService;
import com.example.common.RpcRequest;
import com.example.common.RpcResponse;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.beans.BeansException;
import org.springframework.cglib.reflect.FastClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 自定义业务处理类
 * 1.将标有@RpcService注解的bean进行缓存
 * 2.接收客户端的请求
 * 3.根据传递过来的beanName从缓存中查找
 * 4.通过反射调用bean方法
 * 5.给客户端响应
 */
@Component
@ChannelHandler.Sharable //@Sharable 注解用来说明ChannelHandler是否可以在多个channel直接共享使用
public class RpcServerHandler extends SimpleChannelInboundHandler<String> implements ApplicationContextAware {

    //必须得是static，如果是非静态变量，启动服务后此缓存为空
    private final static Map<String, Object> SERVICE_INSTANCE_MAP = new HashMap<>();

    /**
     * 通道读取就绪事件 - 读取客户端的消息
     * @param cxt
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext cxt, String msg) throws Exception {
        //2.接收客户端的请求
        RpcRequest rpcRequest = JSON.parseObject(msg, RpcRequest.class);
        RpcResponse rpcResponse = new RpcResponse();
        rpcResponse.setRequestId(rpcRequest.getRequestId());
        //业务处理
        try {
            rpcResponse.setResult(handle(rpcRequest));
        } catch (Exception e) {
            e.printStackTrace();
            rpcResponse.setError(e.getMessage());
        }
        //5.给客户端响应
        cxt.writeAndFlush(JSON.toJSONString(rpcResponse));
    }

    private Object handle(RpcRequest rpcRequest) throws InvocationTargetException {
        //3.根据传递过来的beanName从缓存中查找
        Object serviceBean = SERVICE_INSTANCE_MAP.get(rpcRequest.getClassName());
        //4.通过反射调用bean方法
        FastClass proxy = FastClass.create(serviceBean.getClass());
        return proxy.invoke(rpcRequest.getMethodName(), rpcRequest.getParameterType(), serviceBean, rpcRequest.getParameters());
    }

    /**
     * 1.将标有@RpcService注解的bean进行缓存
     * @param context
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        //获取标有@Rpcservice注解的bean集合
        Map<String, Object> beanMap = context.getBeansWithAnnotation(RpcService.class);
        //遍历
        Set<Map.Entry<String, Object>> entrySet = beanMap.entrySet();
        for (Map.Entry<String, Object> entry : entrySet) {
            Object serviceBean = entry.getValue();
            if (serviceBean.getClass().getInterfaces().length == 0) {
                throw new RuntimeException("对外暴露的服务必须实现接口");
            }
            //默认用实现的第一个接口名作为key
            SERVICE_INSTANCE_MAP.put(serviceBean.getClass().getInterfaces()[0].getName(), serviceBean);
        }
    }
}
