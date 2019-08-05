package org.sfs.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.sfs.common.Message;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
//在调用的时候初始化，在comsumer客户端关闭的时候关闭资源
public class ChannelFutureDriven {
    private static ChannelFutureDriven instance = null;
    private ChannelFutureDriven(){}
    private EventLoopGroup group = new NioEventLoopGroup();
    private Bootstrap bootstrap = new Bootstrap();
    private ChannelFuture cf = null;
    private BlockingQueue<Message> blockingQueue = new LinkedBlockingDeque<Message>();
    static {
        if(instance == null){
            synchronized (ChannelFutureDriven.class){
                if(instance == null){
                    instance = new ChannelFutureDriven();
                    instance.init();
                }
            }
        }
    }

    public static ChannelFutureDriven getInstance(){
        return instance;
    }

    public Object getResult(){
        try {
            return blockingQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void init(){
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast(new ReadTimeoutHandler(10));
                        pipeline.addLast(MarshallingCodeFactory.buildMarshallingDecoder());
                        pipeline.addLast(MarshallingCodeFactory.buildMarshallingEncoder());
                        pipeline.addLast(new NettyClientHandler());
                    }
                });
    }

    public ChannelFuture createChannelFuture(URL url){
        try {
            return bootstrap.connect(url.getHostName(),url.getPort()).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ChannelFuture getChannelFuture(URL url){
        if(this.cf == null){
            this.cf = createChannelFuture(url);
        }else if(this.cf.channel().isActive()){
            this.cf = createChannelFuture(url);
        }
        return this.cf;
    }

    private class NettyClientHandler extends SimpleChannelInboundHandler<Message> {
        @Override
        protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message message) throws Exception {
            blockingQueue.put(message);
        }
    }

    public void shutdownGracefully(){
        group.shutdownGracefully();
    }
}
