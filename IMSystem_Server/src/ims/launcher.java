package ims;

import ims.service.Server;

public class launcher {
    public static void main(String[] args) {
        new Server();
    }
}


//1.关于IO流close的时机？
//2.可以多个方法使用同一个IO流吗？例如Client的UCS
//3.目前很麻烦实现退出账号后可以重新登录而不用重启程序，因为服务端一起关闭了客户端的socket和thread，而该功能需要客户端关闭thread而不关闭socket，因此会报异常
//4.离线留言
//5.群发文件会空指针异常，且发生异常后菜单功能失效。但是私聊文件或群发消息没问题？？？--> 此处先取消群发文件功能，找不出bug
//6.可设置消息类型区分群聊私聊消息，此处先不设置
//7.实现文件接收端指定路径保存 --> 接收之前在CTST类run方法的if语句里用ReadString方法接收保存路径出现如下异常，不知如何解决
    /*
     * Exception in thread "Thread-0" java.lang.IndexOutOfBoundsException: end
     * at java.base/java.util.regex.Matcher.region(Matcher.java:1515)
     * at java.base/java.util.Scanner.getCompleteTokenInBuffer(Scanner.java:986)
     * at java.base/java.util.Scanner.next(Scanner.java:1469)
     * at ims.util.Utility.ReadString(Utility.java:8)
     * at ims.service.ClientToServerThread.run(ClientToServerThread.java:39)
     */
//8.可以在User类中加入一个属性体现在线状态。因需要两端同步上下线，故先不再设置
//9.离线消息未写入本地文件，服务器关闭后离线消息集合清空。此处不再将Vector outlineMessage写入本地文件
//10.保存/查询聊天记录功能
