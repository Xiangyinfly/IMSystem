package ims.service;

import ims.element.Message;
import ims.element.MessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;


public class ServerToClientThread extends Thread {
    private Socket socket;
    private String id;
    private boolean loop = true;


    public ServerToClientThread(Socket socket, String id) {
        this.socket = socket;
        this.id = id;
    }

    @Override
    public void run() {
        while (loop) {
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                Message message = (Message) objectInputStream.readObject();

                //向客户端返回在线用户列表
                if (message.getMessageType().equals(MessageType.REQUEST_ONLINE_LIST)) {
                    sendOnlineList();
                }
                //实现无异常退出
                if (message.getMessageType().equals(MessageType.CLIENT_LOGOUT)) {
                    clientLogout();
                }
                //检测是否在线
                if (message.getMessageType().equals(MessageType.REQUEST_EXIST_STATUS)) {
                    checkExist(message);
                }
                //转发私聊/群聊信息/文件
                if (message.getMessageType().equals(MessageType.COMMON_MESSAGE)
                        || message.getMessageType().equals(MessageType.FILE_MESSAGE)) {
                    if (message.getReceiver().equals("*")) {
                        forwardPublicMessage(message);
                    } else {
                        forwardPrivateMessage(message);
                    }
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void sendMessage(String id, Message message) {
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                    ManageSTCThread.getSTCThread(id).getSocket().getOutputStream()
            );
            objectOutputStream.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendOnlineList() {
        System.out.println(id + " 尝试获取在线用户列表...");

        //获得在线用户列表并写入String
        String onlineList = "";
        for (String id:ManageSTCThread.getSTCThreads().keySet()) {
            onlineList = onlineList + id + "  ";
        }

        //构建Message
        Message sentMessage = new Message();
        sentMessage.setMessageType(MessageType.RETURN_ONLINE_LIST);
        sentMessage.setContent(onlineList);
        sentMessage.setReceiver(id);

        //返回Message
        sendMessage(id,sentMessage);
    }

    public void clientLogout() {
        System.out.println(id + " 退出登录...");

        try {
            Thread.sleep(50);
            ManageSTCThread.removeSTCThread(id);
            socket.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        loop = false;
    }

    public void checkExist(Message message) {
        Message sentMessage = new Message();
        sentMessage.setReceiver(id);
        sentMessage.setMessageType(MessageType.RETURN_EXIST_STATUS);
        if (ValidUserData.getUsers().containsKey(message.getContent())) {
            sentMessage.setContent("true");
        } else {
            sentMessage.setContent("false");
        }

        sendMessage(id,sentMessage);
    }

    public boolean checkOnline(String id) {
        return ManageSTCThread.getSTCThreads().containsKey(id);
    }

    public void forwardPrivateMessage(Message message) {
        if (checkOnline(message.getReceiver())) {
            sendMessage(message.getReceiver(), message);
        } else {
            OutlineMessage.getOutlineMessage().add(message);
        }
    }

    public void forwardPublicMessage(Message message) {
        for (String id : ManageSTCThread.getSTCThreads().keySet()) {
            if (! id.equals(message.getSender())) {
                if (checkOnline(message.getReceiver())) {
                    sendMessage(id, message);
                } else {
                    OutlineMessage.getOutlineMessage().add(message);
                }
            }
        }
    }

    public Socket getSocket() {
        return socket;
    }
}
