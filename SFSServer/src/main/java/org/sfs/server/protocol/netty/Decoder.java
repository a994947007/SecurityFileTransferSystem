package org.sfs.server.protocol.netty;

import java.util.List;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * 解码
 */
public class Decoder extends ByteToMessageDecoder {
    private static final int LENGTH = 4;

    /**
     * 解码时传进来的参数是ByteBuf也就是Netty的基本单位，而传出的是解码后的Object
     */
    @Override
    protected void decode(ChannelHandlerContext arg0, ByteBuf in, List<Object> out) throws Exception {
        Utils util = new Utils();
        int size = in.readableBytes();
        // 这里判断大小是为了tcp的粘包问题，这个之后再单独学习
        if (size > Decoder.LENGTH) {
            byte[] bytes = util.getByteFromBuf(in);
            Object info = util.decode(bytes);
            out.add(info);
        }

    }
}
