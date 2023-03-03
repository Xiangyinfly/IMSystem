package ims.service;


import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ValidUserData {
    //ConcurrentHashMap是线程安全的
    private static ConcurrentHashMap<String,String> users = new ConcurrentHashMap<>();
    private static String dataFile = "UserData.txt";
    private static BufferedWriter bufferedWriter = null;
    private static BufferedReader bufferedReader = null;

    public static void getUsersData(String id,String password) {
        users.put(id, password);
    }

    public static void saveUsersData() {
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(dataFile));

            Iterator<Map.Entry<String,String>> iterator = users.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String,String> entry= iterator.next();
                String data = entry.getKey() + "  " + entry.getValue();
                bufferedWriter.write(data + "\r\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static void readUserData() {
        try {
            bufferedReader = new BufferedReader(new FileReader(dataFile));

            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                String[] data = line.split("  ");
                users.put(data[0],data[1]);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static ConcurrentHashMap<String, String> getUsers() {
        return users;
    }
}
