package org.sfs.protocol.cryption;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class RSAEncryption implements Encryption{

    /**
     *
     * @param data     加密数据
     * @param key       公钥
     * @return
     */
    @Override
    public byte[] encryption(byte[] data, byte[] key) {
        try {
            return RSAUtils.publicEncrypt(new String(data),RSAUtils.getPublicKey(new String(key))).getBytes();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param data  解密数据
     * @param key   私钥
     * @return
     */
    @Override
    public byte[] decryption(byte[] data, byte[] key) {
        try {
            return RSAUtils.privateDecrypt(new String(data),RSAUtils.getPrivateKey(new String(key))).getBytes();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }
}
