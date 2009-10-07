package org.ebooksearchtool.analyzer.utils;

import java.io.*;

/**
 * @author Алексей
 */

public class NetUtils {
    public static void sendMessage(OutputStream os, String s) throws IOException {
        for(int i = 0; i < s.length(); i++)
        {
          os.write((byte)s.charAt(i));
        }
        os.write('\n');
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

   public static String reciveMessage(InputStream is) throws IOException {
        byte[] input = new byte[255];
        StringBuilder str = new StringBuilder();
        while(str.indexOf(";") == -1){
            str.append(is.read(input));
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
