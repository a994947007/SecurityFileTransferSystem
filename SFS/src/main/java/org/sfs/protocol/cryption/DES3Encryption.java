package org.sfs.protocol.cryption;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class DES3Encryption implements Encryption{

    @Override
    public byte[] encryption(byte[] data, byte[] key) {
        SecretKey secretKey = new SecretKeySpec(key, "DESede");//恢复密钥
        Cipher cipher = null;//Cipher完成加密或解密工作类
        try {
            cipher = Cipher.getInstance("DESede");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);//对Cipher初始化，解密模式
            byte[] cipherByte = cipher.doFinal(data);//加密data
            return cipherByte;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public byte[] decryption(byte[] data, byte[] key) {
        SecretKey secretKey = new SecretKeySpec(key, "DESede");//恢复密钥
        Cipher cipher = null;//Cipher完成加密或解密工作类
        try {
            cipher = Cipher.getInstance("DESede");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);//对Cipher初始化，解密模式
            byte[] cipherByte = cipher.doFinal(data);//解密data
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }
}
