package org.ebooksearchtool.analyzer.utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.SocketAddress;
import org.ebooksearchtool.analyzer.io.Logger;
import org.ebooksearchtool.analyzer.model.BookInfo;
import org.ebooksearchtool.analyzer.network.ServerConnector;

/**
 * @author Алексей
 */

public class NetUtils {
    public static void sendMessage(HttpURLConnection connect, String s) throws IOException {
        connect.setDoInput(true);
        connect.setDoOutput(true);
        connect.setRequestMethod("POST");
        connect.setRequestProperty( "Content-type", "application/x-www-form-urlencoded; charset=UTF-8" );
        connect.setRequestProperty( "Content-length", String.valueOf(getContentLength(s)));
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
        while(str.indexOf("</response>") == -1 && str.indexOf("nullnullnullnull") == -1){
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

   public static long getContentLength(String str){
        return str.length() + 4;
   }

   public static boolean serverAnswersAnalyze(String message){
       if(message.indexOf("<status>ok") != -1){
           return true;
       }
       return false;
   }


    public static void sendBookInfo(BookInfo info){
        if(!info.getTitle().equals("")){
            String message = ServerConnector.sendRequest
                    (BookInfoFormer.formBookInfo(info), ServerConnector.INSERT_REQUEST);
            if(NetUtils.serverAnswersAnalyze(message)){
                Logger.setToLog("Book Information succsesfully sent to server:" +
                        AnalyzerProperties.getPropertie("system_separator") +
                        AnalyzeUtils.bookInfoToString(info) +
                        AnalyzerProperties.getPropertie("system_separator") +
                        "Server answer is: " +
                        AnalyzerProperties.getPropertie("system_separator") +
                        message);
            }else{
                Logger.setToLog("Book Information don't sent to server:" +
                        AnalyzerProperties.getPropertie("system_separator") +
                        message);
            }
            System.out.println(message);
        }else{
            Logger.setToLog("Book Information can't be sent to server(Unknown title):" +
                    AnalyzerProperties.getPropertie("system_separator") + AnalyzeUtils.bookInfoToString(info));
        }
    }

    //<editor-fold defaultstate="collapsed" desc="Proxy utils">
    public static Proxy proxyInit(){
        SocketAddress addr= new InetSocketAddress(AnalyzerProperties.getPropertie("proxy_host"),
                AnalyzerProperties.getPropertieAsNumber("proxy_port"));
        if(AnalyzerProperties.getPropertieAsBoolean("proxy_enable")){
            return Proxy.NO_PROXY;
        }

        Type proxyType = null;
        String type = AnalyzerProperties.getPropertie("proxy_type");
        if(type.equals("http")){
            proxyType = Proxy.Type.HTTP;
        }else{
            if(type.equals("socks")){
                proxyType = Proxy.Type.SOCKS;
            }else{
                proxyType = Proxy.Type.DIRECT;
            }
        }
        return new Proxy(proxyType, addr);
    }

    public static Proxy serverProxyInit(){
        SocketAddress addr= new InetSocketAddress(AnalyzerProperties.getPropertie("server_proxy_host"),
                AnalyzerProperties.getPropertieAsNumber("server_proxy_port"));
        if(AnalyzerProperties.getPropertieAsBoolean("server_proxy_enable")){
            return Proxy.NO_PROXY;
        }

        Type proxyType = null;
        String type = AnalyzerProperties.getPropertie("server_proxy_type");
        if(type.equals("http")){
            proxyType = Proxy.Type.HTTP;
        }else{
            if(type.equals("socks")){
                proxyType = Proxy.Type.SOCKS;
            }else{
                proxyType = Proxy.Type.DIRECT;
            }
        }
        return new Proxy(proxyType, addr);
    }
    //</editor-fold>
}
