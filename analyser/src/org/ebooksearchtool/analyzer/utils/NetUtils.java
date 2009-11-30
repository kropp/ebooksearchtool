package org.ebooksearchtool.analyzer.utils;

import java.io.*;
import java.net.HttpURLConnection;

/**
 * @author Алексей
 */

public class NetUtils {
    public static void sendMessage(HttpURLConnection connect, String s) throws IOException {
        connect.setDoInput(true);
        connect.setDoOutput(true);
        connect.setRequestMethod("POST");
        connect.setRequestProperty( "Content-type", "application/x-www-form-urlencoded" );
        connect.setRequestProperty( "Content-length", String.valueOf(ServerRequests.getContentLength(s)));
        PrintWriter pw = new PrintWriter(connect.getOutputStream());
        pw.println(s);
        pw.flush();
        pw.close();
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

   public static String reciveCrawlerMessage(BufferedReader is) throws IOException {
        String input = "";
        StringBuilder str = new StringBuilder();
        while(str.indexOf("</root>") == -1){
            input = is.readLine();
            str.append(input);
        }
        return str.toString();
    }

   public static String reciveServerMessage(HttpURLConnection connect) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(connect.getInputStream()));
        String input = "";
        StringBuilder str = new StringBuilder();
        while(str.indexOf("</response>") == -1 ){
            input = br.readLine();
            str.append(input);
        }
        br.close();
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
