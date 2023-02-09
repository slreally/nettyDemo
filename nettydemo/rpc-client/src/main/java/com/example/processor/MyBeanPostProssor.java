package com.example.processor;

import com.example.anno.RpcReference;
import com.example.proxy.RpcClientProxy;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

/**
 * Bean后置增强
 * 每一个bean都会经过这些方法
 */
@Component
public class MyBeanPostProssor implements BeanPostProcessor {

    @Autowired
    RpcClientProxy rpcClientProxy;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        //1 获取bean的字段集合
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            //2 查找bean字段中是否包含这个注解
            RpcReference annotation = field.getAnnotation(RpcReference.class);
            if (annotation != null) {
                //3 获取代理对象
                Object proxy = rpcClientProxy.getProxy(field.getType());
                //4 属性注入
                try {
                    //屏蔽对象的访问检查
                    field.setAccessible(true);
                    //bean对象的这个Field属性设置新值proxy，var1:要修改此字段的对象 var2:此字段的新值
                    field.set(bean, proxy);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return bean;
    }
}
