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

public class ServerConnector extends Thread{

    public static final int GET_REQUEST = 0;
    public static final int INSERT_REQUEST = 1;

    private static URL myInsertURL;
    private static URL myGetURL;
    private static HttpURLConnection myConnection;
    private static final Object myLock = new Object();

    public ServerConnector(String address, int port) throws MalformedURLException{
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

            //TODO:Доделать после поддержки сервером
            //Server Timeout connection
            System.out.println("Server connected on:");
            System.out.println(myInsertURL);
            System.out.println("");
            Logger.setToLog("Server connected on: " + myInsertURL);
//            while(!isConnectionEstablished()){
//                synchronized(myLock){
//                    try {
//                        myLock.wait(AnalyzerProperties.getPropertieAsNumber("serverConnectionTimeout"));
//                    } catch (InterruptedException ex) {
//                        Logger.setToErrorLog(ex.getMessage() + ". ServerConnector thread was interrupted.");
//                    }
//                }
//            }

            while(true){
                synchronized(myLock){
                    try {
                        myLock.wait();
                    } catch (InterruptedException ex) {
                        Logger.setToErrorLog(ex.getMessage() + ". ServerConnector thread was interrupted.");
                    }
                }
            }
        } catch (IOException ex) {
            Logger.setToErrorLog(ex.getMessage() +
                    ". Connection to server failed in client thread initialization.");
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
                Logger.setToErrorLog(ex.getMessage() + ". Connection to server failed in request sending.");
            }
        } catch (NullPointerException ex){
            Logger.setToErrorLog("No server connection found. Please chek the ServerConnector.");
        }
        if(message.length() == 0){
            return "Error in reciving response. Message from server is empty.";
        }

        return message;
    }

    private boolean isConnectionEstablished(){
        String message = ServerConnector.sendRequest
                                (BookInfoFormer.initRequest(), ServerConnector.INSERT_REQUEST);

        if(NetUtils.serverAnswersAnalyze(message)){
            System.out.println("Server connected on:");
            System.out.println(myInsertURL);
            System.out.println("");
            Logger.setToLog("Server connected on: " + myInsertURL);
            return true;
        }
        return false;
    }
}
