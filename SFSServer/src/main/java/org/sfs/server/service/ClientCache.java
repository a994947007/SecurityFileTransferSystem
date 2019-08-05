package org.sfs.server.service;

import io.netty.channel.Channel;
import org.sfs.common.EncryptionMsg;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientCache {
    private static volatile ClientCache instance = null;
    private ClientCache(){}
    static{
        if(instance == null){
            synchronized (ClientCache.class){
                if(instance == null){
                    instance = new ClientCache();
                }
            }
        }
    }

    public static ClientCache getInstance(){
        return instance;
    }

    public String getIp(String clientId){
        return cache.get(clientId).getIp();
    }

    private Map<String,Entry> cache = new ConcurrentHashMap<String,Entry>();

    public void addEntry(String clientId,String ip,EncryptionMsg encryptionMsg){
        cache.put(clientId,new Entry(clientId,ip,encryptionMsg));
    }

    public void setEncryptionMsg(String clientId,EncryptionMsg encryptionMsg){
        cache.get(clientId).setEncryptionMsg(encryptionMsg);
    }

    public void setChx(String clientId, Channel chx){
        cache.get(clientId).setChx(chx);
    }

    public Channel getChx(String clientId){
        return cache.get(clientId).getChx();
    }

    public EncryptionMsg getEncrytionMsg(String clientId){
        return cache.get(clientId).getEncryptionMsg();
    }

    private class Entry{
        private String clientId;
        private String ip;
        private EncryptionMsg encryptionMsg;
        private Channel chx;
        public void setChx(Channel chx){
            this.chx = chx;
        }

        public Channel getChx(){
            return this.chx;
        }

        public String getClientId() {
            return clientId;
        }

        public void setClientId(String clientId) {
            this.clientId = clientId;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public EncryptionMsg getEncryptionMsg() {
            return encryptionMsg;
        }

        public void setEncryptionMsg(EncryptionMsg encryptionMsg) {
            this.encryptionMsg = encryptionMsg;
        }

        public Entry(String clientId, String ip, EncryptionMsg encryptionMsg) {
            this.clientId = clientId;
            this.ip = ip;
            this.encryptionMsg = encryptionMsg;
        }
    }
}
