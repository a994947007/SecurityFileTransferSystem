package org.sfs.common;

import java.io.Serializable;

public class Message implements Serializable {
    private MessageType messageType;
    private Object data;

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Message(MessageType messageType, Object data) {
        this.messageType = messageType;
        this.data = data;
    }
}
