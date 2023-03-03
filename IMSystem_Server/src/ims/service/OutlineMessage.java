package ims.service;

import ims.element.Message;

import java.util.Vector;

public class OutlineMessage {
    private static Vector<Message> outlineMessage = new Vector<>();

    public static Vector<Message> getOutlineMessage() {
        return outlineMessage;
    }

    public static void setOutlineMessage(Vector<Message> outlineMessage) {
        OutlineMessage.outlineMessage = outlineMessage;
    }
}
