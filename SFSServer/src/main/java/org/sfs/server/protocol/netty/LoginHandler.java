package org.sfs.server.protocol.netty;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.sfs.common.Message;
import org.sfs.common.MessageType;
import org.sfs.server.service.ClientCache;

import java.net.InetSocketAddress;

public class LoginHandler extends SimpleChannelInboundHandler<Message> {

    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
        System.out.println("1");

        if(msg.getMessageType().equals(MessageType.LOGIN_REQUEST)){
            ClientCache clientCache = ClientCache.getInstance();
            String clientId = msg.getData().toString();
            String ip = ((InetSocketAddress)ctx.channel().remoteAddress()).getAddress().getHostAddress();
            clientCache.addEntry(clientId,ip,null);
            ctx.writeAndFlush(new Message(MessageType.LOGIN_RESPONSE,"success"));
            System.out.println("2");
        }else{
            ctx.fireChannelRead(msg);
        }
    }
}
