package com.example.common;

import lombok.Data;

/**
 * 封装的请求
 */
@Data
public class RpcRequest {
    /**
     * 请求对象id
     */
    private String requestId;
    /**
     * 类名
     */
    private String className;
    /**
     * 方法名
     */
    private String methodName;
    /**
     * 参数类型
     */
    private Class<?>[] parameterType;
    /**
     * 参数列表
     */
    private Object[] parameters;
}
