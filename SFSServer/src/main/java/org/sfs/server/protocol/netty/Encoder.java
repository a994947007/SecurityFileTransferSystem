package org.sfs.server.protocol.netty;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * netty中的编码
 *
 */
public class Encoder extends MessageToByteEncoder<Object> {
    /**
     * 我们知道在计算机中消息的交互都是byte 但是在netty中进行了封装所以在netty中基本的传递类型是ByteBuf
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, Object in, ByteBuf out) throws Exception {
        Utils util = new Utils();
        byte[] bytes = util.encodes(in);
        out.writeBytes(bytes);
    }

}
