package com.example;

import com.alibaba.fastjson.JSON;
import com.example.pojo.User;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import junit.framework.TestCase;
import org.junit.jupiter.api.Test;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
    @Test
    public void test() {
        User user = new User();
        user.setId(1);
        user.setName("zhangsan");
        byte[] bytes = JSON.toJSONBytes(user);
        ByteBuf byteBuf = Unpooled.buffer();
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);

        System.out.println(byteBuf.readableBytes());

        if (byteBuf.readableBytes() >= 4) {
            int beginIndex = byteBuf.readerIndex();
            int length = byteBuf.readInt() - 4;
            if (byteBuf.readableBytes() < length) {
                byteBuf.readerIndex(beginIndex);
            } else {
                byteBuf.readerIndex(beginIndex + length + 4);
                ByteBuf otherByteBuf = byteBuf.slice(beginIndex, 4 + length);
                otherByteBuf.retain();
                System.out.println(ByteBufUtil.hexDump(otherByteBuf));
            }
        }
    }
}
