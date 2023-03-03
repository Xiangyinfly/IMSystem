package ims.service;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class ManageSTCThread {
    private static ConcurrentHashMap<String,ServerToClientThread> STCThreads= new ConcurrentHashMap<>();

    public static void addSTCThread(String id,ServerToClientThread STCThread){
        STCThreads.put(id,STCThread);
    }

    public static void removeSTCThread(String id) {
        STCThreads.remove(id);
    }

    public static ServerToClientThread getSTCThread(String id){
        return STCThreads.get(id);
    }

    public static ConcurrentHashMap<String, ServerToClientThread> getSTCThreads() {
        return STCThreads;
    }
}
