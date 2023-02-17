package com.example.pojo;

import lombok.Data;

/**
 * 自定义协议包
 */
@Data
public class MessageProtocol {
    /**
     * 一次发送包体长度
     */
    private int len;
    /**
     * 一次发送包体内容
     */
    private byte[] content;
}
