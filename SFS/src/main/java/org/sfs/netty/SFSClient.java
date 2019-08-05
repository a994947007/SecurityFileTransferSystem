package org.sfs.netty;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class SFSClient {
    public Object send(URL url,Object object){
        ChannelFutureDriven driven = ChannelFutureDriven.getInstance();
        ChannelFuture channelFuture = driven.createChannelFuture(url);
        channelFuture.channel().writeAndFlush(object);
        Object result = driven.getResult();
        channelFuture.addListener(ChannelFutureListener.CLOSE);
        return result;
    }

    public Object getResult(){
        return ChannelFutureDriven.getInstance().getResult();
    }
}
