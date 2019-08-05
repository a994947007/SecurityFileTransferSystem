package org.sfs.protocol.cryption;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class SecretKeyFactory {
    public static byte [] createKey(String type){
        if(type.equals("DES")){
            return createDESKey();
        }else if(type.equals("AES")){
            return createAESKey();
        }else if(type.equals("3DES")){
            return create3DESKey();
        }
        return null;
    }

    public static byte[] createDESKey(){
        KeyGenerator keyGen = null;//密钥生成器
        try {
            keyGen = KeyGenerator.getInstance("DES");
            keyGen.init(56);//初始化密钥生成器
            SecretKey secretKey = keyGen.generateKey();//生成密钥
            byte[] key = secretKey.getEncoded();//密钥字节数组
            return key;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] createAESKey(){
        KeyGenerator keyGen = null;//密钥生成器
        try {
            keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(128);//初始化密钥生成器
            SecretKey secretKey = keyGen.generateKey();//生成密钥
            byte[] key = secretKey.getEncoded();//密钥字节数组
            return key;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] create3DESKey(){
        KeyGenerator keyGen = null;//密钥生成器
        try {
            keyGen = KeyGenerator.getInstance("DESede");
            keyGen.init(168);//初始化密钥生成器
            SecretKey secretKey = keyGen.generateKey();//生成密钥
            byte[] key = secretKey.getEncoded();//密钥字节数组
            return key;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static final String CHARSET = "UTF-8";
    public static final String RSA_ALGORITHM = "RSA";

    public static Map<String, String> createKeys(int keySize){
        KeyPairGenerator kpg;
        try{
            kpg = KeyPairGenerator.getInstance(RSA_ALGORITHM);
        }catch(NoSuchAlgorithmException e){
            throw new IllegalArgumentException("No such algorithm-->[" + RSA_ALGORITHM + "]");
        }

        kpg.initialize(keySize);
        KeyPair keyPair = kpg.generateKeyPair();
        Key publicKey = keyPair.getPublic();
        String publicKeyStr = new String(Base64.encodeBase64(publicKey.getEncoded()));
        Key privateKey = keyPair.getPrivate();
        String privateKeyStr = new String( Base64.encodeBase64(privateKey.getEncoded()));
        Map<String, String> keyPairMap = new HashMap<String, String>();
        keyPairMap.put("publicKey", publicKeyStr);
        keyPairMap.put("privateKey", privateKeyStr);

        return keyPairMap;
    }
}
