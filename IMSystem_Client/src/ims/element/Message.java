package ims.element;

import java.io.Serializable;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    private String sender;
    private String receiver;
    private String content;
    private String time;
    private String messageType;

    private String targetPath;
    private byte[] fileBytes;

    public Message() {}

    public Message(String sender, String receiver, String content, String time, String messageType) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.time = time;
        this.messageType = messageType;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public void setTargetPath(String targetPath) {
        this.targetPath = targetPath;
    }

    public void setFileBytes(byte[] fileBytes) {
        this.fileBytes = fileBytes;
    }

    public String getTargetPath() {
        return targetPath;
    }

    public byte[] getFileBytes() {
        return fileBytes;
    }
}
