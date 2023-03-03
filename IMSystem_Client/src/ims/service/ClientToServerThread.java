package ims.service;

import ims.element.Message;
import ims.element.MessageType;

import java.io.*;
import java.net.Socket;

public class ClientToServerThread extends Thread{
    private Socket socket;
    public static boolean check;
    //这地方处理的不好，如何让外部类得到server回复的消息？

    @Override
    public void run() {
        while (true) {
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                Message message = (Message) objectInputStream.readObject();//如果没有信息发送，线程会阻塞在此

                //接收并打印在线用户列表
                if (message.getMessageType().equals(MessageType.RETURN_ONLINE_LIST)) {
                    receiveOnlineList(message);
                }
                if (message.getMessageType().equals(MessageType.RETURN_EXIST_STATUS)) {
                    if (message.getContent().equals("true")) {
                        check = true;
                    } else {
                        System.out.println("您输入的用户不存在...");
                        check = false;
                    }
                }
                if (message.getMessageType().equals(MessageType.COMMON_MESSAGE)) {
                    System.out.println("(" + message.getTime() + ")  " + message.getSender() + ":" + message.getContent());
                }
                if (message.getMessageType().equals(MessageType.FILE_MESSAGE)) {
                    receiveFile(message);
                    System.out.println("(" + message.getTime() + ")  " + message.getSender()+" 发送的文件已经传送到路径 " + message.getTargetPath() + " ...");
                }
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

        }
    }

    public void receiveOnlineList(Message message) {
        String[] onlineUsers = message.getContent().split("  ");
        System.out.println("\n==================在线用户列表==================");
        for (String user :onlineUsers) {
            System.out.println("用户：" + user);
        }
    }

    public void receiveFile(Message message) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(message.getTargetPath(),true);
            fileOutputStream.write(message.getFileBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public ClientToServerThread(Socket socket){
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }
}
