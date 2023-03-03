package ims.service;

import ims.element.Message;
import ims.element.MessageType;
import ims.element.User;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private ServerSocket serverSocket = null;

    public Server(){
        try {
            //读取用户数据
            ValidUserData.readUserData();

            //设置推送线程
            Thread sendServerMessage = new Thread(new SendServerMessage());
            sendServerMessage.setDaemon(true);
            sendServerMessage.start();

            serverSocket = new ServerSocket(9999);

            while (true) {
                Socket socket = serverSocket.accept();
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                User user = (User) objectInputStream.readObject();

                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

                Message ServerMessage = new Message();

                if (user.getMessageType().equals(MessageType.LOGIN)) {
                    login(user,objectOutputStream,ServerMessage,socket);
                }
                if (user.getMessageType().equals(MessageType.REGISTER)) {
                    register(user,objectOutputStream,ServerMessage,socket);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    //接入用户数据，判断是否有该用户或者已注册过
    private boolean checkLoginUser(User user) {
        return ValidUserData.getUsers().containsKey(user.getId())
                && ValidUserData.getUsers().get(user.getId()).equals(user.getPassword());
    }

    private boolean checkRegisterUser(User user) {
        return !ValidUserData.getUsers().containsKey(user.getId());
    }

    private void login(User user,ObjectOutputStream objectOutputStream,Message ServerMessage,Socket socket) {
        try {
            if (checkLoginUser(user)) {
                ServerMessage.setMessageType(MessageType.LOGIN_SUCCEED);
                objectOutputStream.writeObject(ServerMessage);
                ServerToClientThread serverToClientThread = new ServerToClientThread(socket, user.getId());
                serverToClientThread.start();
                ManageSTCThread.addSTCThread(user.getId(),serverToClientThread);

                System.out.println("用户 id = " + user.getId() + "   password = " + user.getPassword() + "已登陆...");

                //检查并发送离线消息
                for (Message message : OutlineMessage.getOutlineMessage()) {
                    if (message.getReceiver().equals(user.getId())) {
                        serverToClientThread.sendMessage(message.getReceiver(), message);
                        OutlineMessage.getOutlineMessage().remove(message);
                    }
                }

            } else {
                ServerMessage.setMessageType(MessageType.LOGIN_FAIL);
                objectOutputStream.writeObject(ServerMessage);
                socket.close();

                System.out.println("用户 id = " + user.getId() + "   password = " + user.getPassword() + "登陆失败...");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void register(User user,ObjectOutputStream objectOutputStream,Message ServerMessage,Socket socket) {
        try {
            if (checkRegisterUser(user)) {
                //将注册用户储存在users
                ValidUserData.getUsersData(user.getId(),user.getPassword());
                ValidUserData.saveUsersData();

                ServerMessage.setMessageType(MessageType.REGISTER_SUCCEED);
                objectOutputStream.writeObject(ServerMessage);

                System.out.println("用户 id = " + user.getId() + "   password = " + user.getPassword() + "注册成功...");
            } else {
                ServerMessage.setMessageType(MessageType.REGISTER_FAIL);
                objectOutputStream.writeObject(ServerMessage);

                System.out.println("用户 id = " + user.getId() + "   password = " + user.getPassword() + "注册失败...");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
