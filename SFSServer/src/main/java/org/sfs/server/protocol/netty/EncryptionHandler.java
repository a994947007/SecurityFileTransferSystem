package org.sfs.server.protocol.netty;

import com.sun.security.ntlm.Client;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.sfs.common.EncryptionMsg;
import org.sfs.common.Message;
import org.sfs.common.MessageType;
import org.sfs.server.service.ClientCache;
import org.sfs.server.service.SecretKeyFactory;

import java.util.Map;


public class EncryptionHandler extends SimpleChannelInboundHandler<Message> {

    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
        if(msg.getMessageType().equals(MessageType.ENCRYPTION_REQUEST_1)){
            String ids[] = msg.getData().toString().split(",");
            String srcId = ids[0];
            String dstId = ids[1];
            //生成非对称加密密钥
            Map<String,String> keys = SecretKeyFactory.createKeys(1024);
            String publicKey = keys.get("publicKey");
            String privateKey = keys.get("privateKey");
            System.out.println("publicKey->>>" + publicKey);
            System.out.println("privateKey->>>" + privateKey);

            String srcIp = ClientCache.getInstance().getIp(srcId);
            String dstIp = ClientCache.getInstance().getIp(dstId);

            ClientCache.getInstance().setEncryptionMsg(srcId,new EncryptionMsg("publicKey",publicKey + ":::" + dstIp));
            ClientCache.getInstance().setEncryptionMsg(dstId,new EncryptionMsg("privateKey",privateKey + ":::" + srcIp));
            ctx.writeAndFlush(new Message(MessageType.ENCRYPTION_RESPONSE,publicKey + ":::" + dstIp));
        }else if(msg.getMessageType().equals(MessageType.ENCRYPTION_REQUEST_2)){
            String clientId = msg.getData().toString();     //返回私钥
            ctx.writeAndFlush(new Message(MessageType.ENCRYPTION_RESPONSE,ClientCache.getInstance().getEncrytionMsg(clientId).getSecretKey()));
        }
    }
}
