package ims.service;

import java.util.HashMap;

public class ManageCTSThread {
    private static HashMap<String,ClientToServerThread> CTSThreads = new HashMap<>();

    public static void addCTSThread(String id,ClientToServerThread CTSThread) {
        CTSThreads.put(id,CTSThread);
    }

    public static ClientToServerThread getCTSThread(String id) {
        return CTSThreads.get(id);
    }
}
