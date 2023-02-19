package com.example.codec;

import com.example.pojo.MessageProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;

import java.util.List;

/**
 * 将二进制字节码 -> MessageProtocol数据包(对象)
 */
public class MessageDecoder extends ByteToMessageDecoder {
    int length = 0;

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> list) throws Exception {
        System.out.println("MessageDecoder decode 被调用");
        if (in.readableBytes() >= 4) {
            if (length == 0) {
                length = in.readInt();
            }
            if (in.readableBytes() < length) {
                System.out.println("当前k可读数据不够，继续等待...");
                return;
            }
            byte[] content = new byte[length];
            if (in.readableBytes() >= length) {
                in.readBytes(content);
                //封装成MessageProtocol对象，交给下一个handler处理
                MessageProtocol messageProtocol = new MessageProtocol();
                messageProtocol.setLen(length);
                messageProtocol.setContent(content);
                list.add(messageProtocol);
                length = 0;
            }
        }
    }
}
