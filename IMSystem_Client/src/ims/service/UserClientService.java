package ims.service;

import ims.element.Message;
import ims.element.MessageType;
import ims.element.User;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Date;

public class UserClientService {
    private User user = new User();
    private Socket socket;

    //用于登陆或注册
    public Message sendUser_L_R(User user) {
        try {
            socket = new Socket(InetAddress.getByName("127.0.0.1"),9999);


            //发送User对象到服务器
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(user);

            //接收服务器返回的Message
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            Message message = (Message) objectInputStream.readObject();
            return message;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean checkLoginUser(String id, String password) {
        user.setId(id);
        user.setPassword(password);
        user.setMessageType(MessageType.LOGIN);
        boolean flag = false;

        Message message =  sendUser_L_R(user);
        //判断是否登陆成功
        if (message.getMessageType().equals(MessageType.LOGIN_SUCCEED)){
            //创建和服务器连接的线程
            ClientToServerThread clientToServerThread = new ClientToServerThread(socket);
            clientToServerThread.setDaemon(true);//设置为守护线程
            clientToServerThread.start();
            ManageCTSThread.addCTSThread(id,clientToServerThread);

            flag = true;
        } else {
            //关闭socket
            try {
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return flag;
    }

    public boolean checkRegisteredUser(String id,String password) {
        user.setId(id);
        user.setPassword(password);
        user.setMessageType(MessageType.REGISTER);
        Message message = sendUser_L_R(user);

        if (message.getMessageType().equals(MessageType.REGISTER_SUCCEED)) {
            return true;
        }
        return false;
    }

    //用于登陆之后，用CTSThread的IO流传输
    public void sendMessage(User user,Message message) {
        try {
            //得到已登陆user对应CTS线程的Socket的OutputStream
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                    ManageCTSThread.getCTSThread(user.getId()).getSocket().getOutputStream()
            );
            objectOutputStream.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void requestOnlineList() {
        Message message = new Message();
        message.setMessageType(MessageType.REQUEST_ONLINE_LIST);
        message.setSender(user.getId());

        sendMessage(user,message);
    }

    public void checkOnline(String id) {
        Message message = new Message();
        message.setSender(user.getId());
        message.setContent(id);
        message.setMessageType(MessageType.REQUEST_EXIST_STATUS);

        sendMessage(user,message);
    }

    public void privateChat(String content,String receiver) {
        Message message = new Message(user.getId(), receiver,content,new Date().toString(),MessageType.COMMON_MESSAGE);

        sendMessage(user,message);
    }

    public void publicChat(String content) {
        Message message = new Message(user.getId(), "*",content,new Date().toString(),MessageType.COMMON_MESSAGE);

        sendMessage(user,message);
    }

    public void fileTransmit(String receiver,String path,String targetPath) {
        FileInputStream fileInputStream = null;
        byte[] fileBytes = new byte[(int)new File(path).length()];
        Message message = new Message();
        message.setSender(user.getId());
        message.setReceiver(receiver);
        message.setMessageType(MessageType.FILE_MESSAGE);
        message.setTime(new Date().toString());
        message.setTargetPath(targetPath);

        try {
            fileInputStream = new FileInputStream(path);
            fileInputStream.read(fileBytes);
            message.setFileBytes(fileBytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        sendMessage(user,message);

        System.out.println("已将文件 " + path + " 传送到 " + receiver + " 的 " + targetPath + " 路径...");
    }

    public void logout() {
        Message message = new Message();
        message.setSender(user.getId());
        message.setMessageType(MessageType.CLIENT_LOGOUT);

        sendMessage(user,message);
        System.out.println(user.getId() + " 已退出登录...");
        System.exit(0);
    }
}
