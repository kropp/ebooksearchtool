package org.ebooksearchtool.analyzer.network;

import java.net.MalformedURLException;
import org.ebooksearchtool.analyzer.utils.NetUtils;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import org.ebooksearchtool.analyzer.io.Logger;
import org.ebooksearchtool.analyzer.utils.AnalyzerProperties;
import org.ebooksearchtool.analyzer.utils.ServerRequests;

/**
 * @author Алексей
 */

public class ClientSocketThread extends Thread{

    private static URL myURL;
    private static HttpURLConnection myConnection;
    private static final Object myLock = new Object();

    public ClientSocketThread(String address, int port) throws MalformedURLException{
        myURL = new URL(AnalyzerProperties.getPropertie("default_protocol") +
                "://" + address + ":" + port +
                AnalyzerProperties.getPropertie("server_insert_distanation"));
        myConnection = null;
    }

    @Override
    public synchronized void run(){
        try {
            myConnection = (HttpURLConnection) myURL.openConnection();
            myConnection.setDoInput(true);
            myConnection.setDoOutput(true);

            System.out.println("Server connected on:");
            System.out.println(myURL);
            System.out.println("");
            Logger.setToLog("Server connected on: " + myURL);

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

    public static synchronized String sendRequest(String request){
        String message = "";
        try {
            try {
                myConnection = (HttpURLConnection) myURL.openConnection();
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
