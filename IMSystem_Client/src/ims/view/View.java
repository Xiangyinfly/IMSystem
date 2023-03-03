package ims.view;


import ims.service.ClientToServerThread;
import ims.service.UserClientService;
import ims.util.Utility;

public class View {

    private String key = "";
    private UserClientService userClientService = new UserClientService();


    public static void main(String[] args) {
        new View().mainMenu();
    }


    private void mainMenu(){
        boolean loop1 = true;

        while (loop1){
            System.out.println("==================欢迎登录通讯系统==================");
            System.out.println("1.登录系统");
            System.out.println("2.注册账号");
            System.out.println("9.退出");
            System.out.print("请输入您的选择：");


            key = Utility.ReadString(1);
            switch (key){
                case "1":
                    System.out.println("请输入用户名：");
                    String id = Utility.ReadString(16);
                    System.out.println("请输入密码：");
                    String password = Utility.ReadString(16);

                        if (userClientService.checkLoginUser(id,password)){
                            System.out.println("==================欢迎 " + id + "==================");
                            try {
                                Thread.sleep(50);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            SubManu1();
                    } else {
                        System.out.println("登录失败...");
                    }
                    break;
                case "2":
                    System.out.println("请输入一个用户名：");
                    String registerId = Utility.ReadString(16);
                    System.out.println("请输入您的密码：");
                    String registerPwd = Utility.ReadString(16);

                    if (userClientService.checkRegisteredUser(registerId,registerPwd)) {
                        System.out.println("==================" + registerId + "注册成功==================");
                    } else {
                        System.out.println("您的用户名已经被使用过，请换一个用户名重新注册...");
                    }
                    break;
                case "9":
                    System.out.println("确定要退出系统？(y/n)");
                    String key_ = Utility.ReadString(1);
                    if (key_.equals("y")){
                        loop1 = false;
                        System.out.println("客户端退出...");
                    }
                    if (! (key_.equals("y") || key_.equals("n"))){
                        System.out.println("请输入y/n...");
                    }
                    break;
                default:
                    System.out.println("请输入指定的序号...");
            }
        }
    }

    private void SubManu1(){
        boolean loop2 = true;
        while (loop2){
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            System.out.println("==================操作选项==================");
            System.out.println("1.在线用户列表");
            System.out.println("2.私聊消息");
            System.out.println("3.群发消息");
            System.out.println("4.发送文件");
            System.out.println("9.退出账号");
            System.out.print("请输入您的选择：");

            key = Utility.ReadString(1);
            switch (key){
                case "1":
                    userClientService.requestOnlineList();
                    break;
                case "2":
                    System.out.println("请输入您想私聊对象的用户名：");
                    String chatObject1 = Utility.ReadString(16);
                    userClientService.checkOnline(chatObject1);
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    if  (ClientToServerThread.check) {
                        System.out.println("\n==================进入私聊==================");
                        System.out.println("提示：退出私聊请输入 *exit ");
                        System.out.println("==================私聊对象:" + chatObject1 + "==================");

                        while (true) {
                            String content = Utility.ReadString(1000);
                            if (content.equals("*exit")) {
                                break;
                            }
                            userClientService.privateChat(content,chatObject1);
                        }
                    }
                    break;
                case "3":
                    System.out.println("\n==================进入群聊==================");
                    System.out.println("提示：退出私聊请输入 *exit ");

                    while (true) {
                        String content = Utility.ReadString(1000);
                        if (content.equals("*exit")) {
                            break;
                        }
                        userClientService.publicChat(content);
                    }
                    break;
                case "4":
                    System.out.println("请输入发送对象：");
                    String chatObject2 = Utility.ReadString(16);
                    userClientService.checkOnline(chatObject2);

                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    if (ClientToServerThread.check) {
                        System.out.println("\n==================进入发文件==================");
                        System.out.println("==================发文件对象:" + chatObject2 + "==================");
                        System.out.println("请输入文件路径：");
                        String path = Utility.ReadString(1000);
                        System.out.println("请输入文件传送目标路径：");
                        String targetPath = Utility.ReadString(1000);
                        userClientService.fileTransmit(chatObject2, path, targetPath);
                    }
                    break;
                case "9":
                    System.out.println("确定要退出账号？(y/n)");
                    String key_ = Utility.ReadString(1);
                    if (key_.equals("y")){
                        loop2 = false;
                        System.out.println("您的账号已退出...");
                        userClientService.logout();
                    }
                    if (! (key_.equals("y") || key_.equals("n"))){
                        System.out.println("请输入y/n...");
                    }
                    break;
                default:
                    System.out.println("请输入指定的序号...");
            }
        }
    }
}
