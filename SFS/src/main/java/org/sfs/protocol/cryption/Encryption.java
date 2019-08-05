package org.sfs.protocol.cryption;

public interface Encryption {
    /**
     * 加密
     * @param data     加密数据
     * @param key       密钥
     */
    public byte[] encryption(byte[] data, byte[] key);

    /**
     * 解密
     * @param data  解密数据
     * @param key   密钥
     */
    public byte[] decryption(byte[] data, byte[] key);
}
