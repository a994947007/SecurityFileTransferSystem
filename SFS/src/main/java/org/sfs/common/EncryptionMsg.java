package org.sfs.common;

import java.io.Serializable;

public class EncryptionMsg implements Serializable {
    private String type;
    private byte[] secretKey;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public byte[] getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(byte[] secretKey) {
        this.secretKey = secretKey;
    }

    public EncryptionMsg(String type, byte[] secretKey) {
        this.type = type;
        this.secretKey = secretKey;
    }
}
