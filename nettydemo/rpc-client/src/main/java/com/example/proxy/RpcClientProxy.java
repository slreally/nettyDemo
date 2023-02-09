package com.example.proxy;

import com.alibaba.fastjson.JSON;
import com.example.client.NettyRpcClient;
import com.example.common.RpcRequest;
import com.example.common.RpcResponse;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 客户端代理
 */
@Component
public class RpcClientProxy {
    private static final Map<Class, Object> SERVICE_PROXY = new HashMap<>();

    @Resource
    NettyRpcClient nettyRpcClient;

    /**
     * 获取代理对象
     * @param serviceClass
     * @return
     */
    public Object getProxy(Class serviceClass) {
        Object proxy = SERVICE_PROXY.get(serviceClass);
        if (proxy == null) {
            //创建代理对象
            proxy = Proxy.newProxyInstance(this.getClass().getClassLoader(),
                    new Class[]{serviceClass}, new InvocationHandler() {
                        @Override
                        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                            //1 封装请求对象
                            RpcRequest rpcRequest = new RpcRequest();
                            rpcRequest.setRequestId(UUID.randomUUID().toString());
                            rpcRequest.setClassName(method.getDeclaringClass().getName());
                            rpcRequest.setMethodName(method.getName());
                            rpcRequest.setParameterType(method.getParameterTypes());
                            rpcRequest.setParameters(args);
                            try {
                                //2 发送消息
                                Object msg = nettyRpcClient.send(JSON.toJSONString(rpcRequest));
                                //3 转化响应消息
                                RpcResponse rpcResponse = JSON.parseObject(msg.toString(), RpcResponse.class);
                                if (rpcResponse.getError() != null) {
                                    throw new RuntimeException(rpcResponse.getError());
                                }
                                if (rpcResponse.getResult() != null) {
                                    return JSON.parseObject(rpcResponse.getResult().toString(), method.getReturnType());
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                throw new RuntimeException(e);
                            }
                            return null;
                        }
                    });
            //放入缓存
            SERVICE_PROXY.put(serviceClass, proxy);
            return proxy;
        } else {
            return proxy;
        }
    }
}
