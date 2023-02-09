package com.example.common;

import lombok.Data;

@Data
public class RpcResponse {
    /**
     * 请求对象id
     */
    private String requestId;
    /**
     * 错误信息
     */
    private String error;
    /**
     * 返回结果
     */
    private Object result;
}
