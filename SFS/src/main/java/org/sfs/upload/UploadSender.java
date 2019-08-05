package org.sfs.upload;

import org.sfs.common.EncryptionMsg;
import org.sfs.common.Message;
import org.sfs.protocol.cryption.*;

import java.io.*;
import java.net.Socket;

public class UploadSender {
    public void send(File file,byte [] key,String type,String ip){
        Thread t = new Thread(new Sender(file,key,type,ip));
        t.setDaemon(true);
        t.start();
    }

    private class Sender implements Runnable{
        private File file;
        private byte [] key;
        private String type;
        private String ip;
        public Sender(File file,byte [] key ,String type,String ip){
            this.file = file;
            this.key = key;
            this.type = type;
            this.ip = ip;
        }
        @Override
        public void run() {
            Socket s = null;
            try {
                 s = new Socket(ip,9889);
                ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
                //发送hello
                oos.writeUTF("hello");
                oos.flush();

                //byte[] secret = SecretKeyFactory.createKey(type);
                byte [] secret = RandomSecretCryption.getRandomSecret();
                //设置当前加密类型
                EncryptionContext.getInstance().setEncryption(new EncryptionMsg(type,secret));
                System.out.println(new String(secret));
                System.out.println("sender-->>>" + new String(secret));
                //RSA加密
                Encryption encryption = new RSAEncryption();
                byte[] _secret = encryption.encryption(secret,key);
                oos.writeUTF(type +":::" + new String(_secret));
                oos.flush();

                //文件名加密
                oos.writeObject(new Message(null,EncryptionContext.getInstance().encryption(file.getName().getBytes())));
                oos.flush();

                FileInputStream fis = new FileInputStream(file);
                byte [] buf = new byte[1024];
                int len = 0;
                while((len = fis.read(buf)) != -1){
                    byte bf[] = null;
                    if(len < 1024){
                        bf = new byte[len];
                        System.arraycopy(buf,0,bf,0,len);
                        buf = bf;
                    }
                    byte [] secretBuf = EncryptionContext.getInstance().encryption(buf);
                    oos.writeObject(new Message(null,secretBuf));
                }
                oos.writeObject(null);
                oos.flush();
            } catch (IOException e) {
                try {
                    s.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}
