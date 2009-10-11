package org.ebooksearchtool.analyzer.utils;

import java.io.*;

/**
 * @author Алексей
 */

public class NetUtils {
    public static void sendMessage(BufferedWriter os, String s) throws IOException {
        os.write(s);
        os.newLine();
        os.flush();
    }

   public static String convertBytesToString(byte[] b){
        int index = 0;
        StringBuilder str = new StringBuilder();
        while(index < b.length){
            str.append((char) b[index]);
            index++;
        }
        return str.toString();
    }

   public static String reciveMessage(BufferedReader is) throws IOException {
        String input = "";
        StringBuilder str = new StringBuilder();
        while(str.indexOf("</root>") == -1){
            input = is.readLine();
            str.append(input);
        }
        return str.toString();
    }

   public static byte[] convertToByteArray(String str){
        char[] temp = str.toCharArray();
        int index = temp.length;
        byte[] bytes = new byte[255];
        for (int i = 0; i < index; i++) {
            bytes[i] = (byte)temp[i];
        }
        return bytes;
    }
}
