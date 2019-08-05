package org.sfs.protocol.cryption;


import org.sfs.common.EncryptionMsg;

public class EncryptionContext {
    private Encryption encryption = null;
    private EncryptionMsg encryptionMsg = null;
    private static volatile EncryptionContext instance = null;
    private EncryptionContext(){}
    static{
        if(instance == null){
            synchronized (EncryptionContext.class){
                if(instance == null){
                    instance = new EncryptionContext();
                }
            }
        }
    }

    public void setEncryption(String type){
        if(type.equals("DES")){
            encryption = new DESEncryption();
        }else if(type.equals("AES")){
            encryption = new AESEncryption();
        }else if(type.equals("3DES")){
            encryption = new DES3Encryption();
        }
    }

    public void setEncryption(EncryptionMsg encryptionMsg){
        this.encryptionMsg = encryptionMsg;
        if(encryptionMsg.getType().equals("DES")){
            encryption = new DESEncryption();
        }else if(encryptionMsg.getType().equals("AES")){
            encryption = new AESEncryption();
        }else if(encryptionMsg.getType().equals("3DES")){
            encryption = new DES3Encryption();
        }
    }

    public static EncryptionContext getInstance(){
        return instance;
    }

    public byte[] decryption(byte [] data){
        byte [] key = encryptionMsg.getSecretKey();
        return encryption.decryption(data,key);
    }

    public byte[] encryption(byte [] data){
        byte [] key = encryptionMsg.getSecretKey();
        return encryption.encryption(data,key);
    }
}
