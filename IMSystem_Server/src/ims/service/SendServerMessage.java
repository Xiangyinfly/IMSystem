package ims.service;

import ims.element.Message;
import ims.element.MessageType;
import ims.util.Utility;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;

public class SendServerMessage extends Thread {
    @Override
    public void run() {
        while (true) {
            System.out.println("请输入推送的消息(输入*exit以退出推送)：");
            String content = Utility.ReadString(1000);

            if (content.equals("*exit")) {
                break;
            }

            Message message = new Message();
            message.setSender("服务器");
            message.setContent(content);
            message.setMessageType(MessageType.COMMON_MESSAGE);
            message.setTime(new Date().toString());
            System.out.println("服务器推送：" + content);

            for (String id : ManageSTCThread.getSTCThreads().keySet()) {
                try {
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                            ManageSTCThread.getSTCThread(id).getSocket().getOutputStream()
                    );
                    objectOutputStream.writeObject(message);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
