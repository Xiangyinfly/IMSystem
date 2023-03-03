package ims.util;

import java.util.Scanner;

public class Utility {
    static Scanner scanner = new Scanner(System.in);
    public static String ReadString(int limit){
        String string = scanner.next();
        if (string.length() <= limit && ! string.equals("*")){
            return string;
        }
        return "0";
    }
}
