package org.sfs.common;

import java.io.Serializable;

public class EncryptionMsg implements Serializable {
    private String type;
    private String secretKey;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public EncryptionMsg(String type, String secretKey) {
        this.type = type;
        this.secretKey = secretKey;
    }
}
