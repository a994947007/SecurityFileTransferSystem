package org.sfs.server.protocol.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * netty SFSServer，用来接收客户终端请求
 */
public class SFSServer {

    private int port;
    public SFSServer(int port){
        this.port = port;
        init();
    }

    public void init(){
        Thread t = new Thread(new Server());
        t.setDaemon(true);
        t.start();
    }

    private EventLoopGroup boss;
    private EventLoopGroup worker;
    private class Server implements Runnable{
        public void run() {
            boss = new NioEventLoopGroup();
            worker = new NioEventLoopGroup();
            try{
                ServerBootstrap serverBootstrap = new ServerBootstrap();
                serverBootstrap.group(boss,worker)
                        .channel(NioServerSocketChannel.class)
                        .option(ChannelOption.SO_BACKLOG,128)
                        .option(ChannelOption.SO_SNDBUF,32*1024)
                        .option(ChannelOption.SO_RCVBUF,32*1024)
                        .childOption(ChannelOption.SO_KEEPALIVE,true);

                serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                       pipeline.addLast(MarshallingCodeFactory.buildMarshallingDecoder());
                        pipeline.addLast(MarshallingCodeFactory.buildMarshallingEncoder());
                        //pipeline.addLast(new Decoder());
                        //pipeline.addLast(new Encoder());
                        pipeline.addLast(new LoginHandler());
                        pipeline.addLast(new EncryptionHandler());
                    }
                });
                ChannelFuture channelFuture =  serverBootstrap.bind(port).sync();          //bind为Callable异步，通过sync()即通过Future切换成同步
                channelFuture.channel().closeFuture().sync();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void shutdown(){
        if(boss!= null) boss.shutdownGracefully();
        if(worker != null) worker.shutdownGracefully();
    }

    public void shutdownNow(){
        if(boss != null) boss.shutdownNow();
        if(worker!= null)worker.shutdownNow();
    }
}
