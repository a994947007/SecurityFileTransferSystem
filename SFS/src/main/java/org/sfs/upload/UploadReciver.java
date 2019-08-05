package org.sfs.upload;

import org.sfs.common.EncryptionMsg;
import org.sfs.common.Message;
import org.sfs.common.MessageType;
import org.sfs.netty.SFSClient;
import org.sfs.netty.URL;
import org.sfs.protocol.cryption.Encryption;
import org.sfs.protocol.cryption.EncryptionContext;
import org.sfs.protocol.cryption.RSAEncryption;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class UploadReciver implements Runnable{
    private SFSClient client = null;
    private String clientId = null;
    public UploadReciver(SFSClient client,String clientId){
        this.client = client;
        this.clientId = clientId;
    }
    @Override
    public void run() {
        //启动ServerSocket服务器
        try {
            ServerSocket serverSocket = new ServerSocket(9889);
            while(true){
                Client c = new Client(serverSocket.accept());
                Thread t = new Thread(c);
                t.setDaemon(true);
                t.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class Client implements Runnable{
        private Socket s;
        private ObjectOutputStream oos;
        private ObjectInputStream ois;
        public Client(Socket s){
            this.s = s;
            try {
                this.oos = new ObjectOutputStream(s.getOutputStream());
                this.ois = new ObjectInputStream(s.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void run() {
            System.out.println("reciver running...");
            while(!Thread.currentThread().isInterrupted()){
                try {
                    String msg = ois.readUTF();
                    String privateKey = null;
                    byte [] secretKey = null;
                    if(msg.equals("hello")){
                        //先去服务器获取密钥
                        Message message = (Message) client.send(new URL("127.0.0.1",9999),new Message(MessageType.ENCRYPTION_REQUEST_2,clientId));
                        String str = message.getData().toString();
                        System.out.println("privateKey->>>" + str);
                        privateKey = str.split(":::")[0];
                        //解密
                        String ssss = ois.readUTF();
                        String type = ssss.split(":::")[0];
                        String key = ssss.split(":::")[1];
                        Encryption encryption = new RSAEncryption();
                        secretKey =  encryption.decryption(key.getBytes(),privateKey.getBytes());
                        EncryptionContext.getInstance().setEncryption(type);
                        EncryptionContext.getInstance().setEncryption(new EncryptionMsg(type,secretKey));
                        System.out.println(new String(secretKey));
                        //解文件
                        byte buf [] = null;
                        try {
                            buf = (byte[]) ((Message)ois.readObject()).getData();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        String fileName = new String(EncryptionContext.getInstance().decryption(buf));
                        System.out.println(fileName);
                        FileOutputStream fos = new FileOutputStream(new File("G:/"+fileName));
                        Message message1 = null;
                        try {
                            while((message1 = (Message)ois.readObject())!= null){
                                byte [] desecretBuf = EncryptionContext.getInstance().decryption((byte[]) message1.getData());
                                fos.write(desecretBuf,0,desecretBuf.length);
                            }
                            fos.flush();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }finally {
                            if(oos!= null){
                                oos.close();
                            }
                            fos.close();
                        }
                    }
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
}
