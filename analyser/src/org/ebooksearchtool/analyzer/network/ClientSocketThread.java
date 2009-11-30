package org.ebooksearchtool.analyzer.network;

import java.net.MalformedURLException;
import org.ebooksearchtool.analyzer.utils.NetUtils;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import org.ebooksearchtool.analyzer.io.Logger;
import org.ebooksearchtool.analyzer.utils.AnalyzerProperties;
import org.ebooksearchtool.analyzer.utils.BookInfoFormer;

/**
 * @author Алексей
 */

public class ClientSocketThread extends Thread{

    public static final int GET_REQUEST = 0;
    public static final int INSERT_REQUEST = 1;

    private static URL myInsertURL;
    private static URL myGetURL;
    private static HttpURLConnection myConnection;
    private static final Object myLock = new Object();

    public ClientSocketThread(String address, int port) throws MalformedURLException{
        myInsertURL = new URL(AnalyzerProperties.getPropertie("default_protocol") +
                "://" + address + ":" + port +
                AnalyzerProperties.getPropertie("server_insert_distanation"));
        myConnection = null;
    }

    @Override
    public synchronized void run(){
        try {
            myConnection = (HttpURLConnection) myInsertURL.openConnection();
            myConnection.setDoInput(true);
            myConnection.setDoOutput(true);

            System.out.println("Server connected on:");
            System.out.println(myInsertURL);
            System.out.println("");
            Logger.setToLog("Server connected on: " + myInsertURL);

            while(true){
                synchronized(myLock){
                    try {
                        myLock.wait();
                    } catch (InterruptedException ex) {
                        Logger.setToErrorLog(ex.getMessage());
                    }
                }
            }
        } catch (IOException ex) {
            Logger.setToErrorLog(ex.getMessage());
        }
}

    public static synchronized String sendRequest(String request, int requestType){
        String message = "";
        try {
            try {
                if(requestType == INSERT_REQUEST){
                    myConnection = (HttpURLConnection) myInsertURL.openConnection();
                }else{
                    myConnection = (HttpURLConnection) myGetURL.openConnection();
                }
                NetUtils.sendMessage(myConnection, request);
                message = URLDecoder.decode(NetUtils.reciveServerMessage(myConnection), "UTF-8");
            } catch (IOException ex) {
                Logger.setToErrorLog(ex.getMessage());
            }
        } catch (NullPointerException ex){
            Logger.setToErrorLog("No server connection found. Please chek the server connection.");
        }
        if(message.length() == 0){
            return "Reciving error. Message is empty.";
        }

        return message;
    }
}
