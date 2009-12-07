package org.ebooksearchtool.analyzer.network;

import org.ebooksearchtool.analyzer.network.demon.DemonThread;
import org.ebooksearchtool.analyzer.utils.NetUtils;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import org.ebooksearchtool.analyzer.io.Logger;
import org.ebooksearchtool.analyzer.utils.AnalyzerProperties;

/**
 * @author Алексей
 */

public class CrawlerConnector extends Thread{

    private ServerSocket myServerSocket = null;
    private Socket mySocket = null;
    private List<AnalyzerThread> myThreads = null;
    private BufferedReader myBR = null;
    private BufferedWriter myBW = null;

    public CrawlerConnector(ServerSocket sSocket){
        myServerSocket = sSocket;
        myThreads = new ArrayList<AnalyzerThread>();
        for (int i = 0; i < AnalyzerProperties.getPropertieAsNumber("number_of_threads"); i++) {
            myThreads.add(new AnalyzerThread());
        }
        for (int i = 0; i < AnalyzerProperties.getPropertieAsNumber("number_of_threads"); i++) {
            myThreads.get(i).start();
        }
    }

    @Override
    public synchronized void run(){
        boolean requestToAnalyzeFlag = false;
        int requestToAnalyzeCount = 0;
        DemonThread dt = new DemonThread();
        dt.start();

        while(true){
            try{
                try{
                    establishConnection();
                    String buffer = "";

                    while(true){
                        buffer = NetUtils.reciveCrawlerMessage(myBR);
                        //Нужно только если тестить//System.out.println(buffer);
                        while(!requestToAnalyzeFlag){
                            if(requestToAnalyzeCount == 10){
                                requestToAnalyzeCount = 0;
                            }
                            requestToAnalyzeFlag = myThreads.get(requestToAnalyzeCount).setMessage(buffer);
                            requestToAnalyzeCount++;
                        }
                        requestToAnalyzeFlag = false;
                    }
                }catch(IOException ex){
                    Logger.setToLog(ex.getMessage() + ". Error in using crawler connection. " +
                            "Chek connection, Analyzer will try to reconnect.");
                    if(myBR != null){
                        myBR.close();
                    }
                    if(myBW != null){
                        myBW.close();
                    }
                    if(mySocket != null){
                        mySocket.close();
                    }
                    if(myServerSocket != null){
                        myServerSocket.close();
                    }
                }
            }catch(IOException ex){
                //This catch only for compilator tranquillity. Exception never thrown.
            }
        }
    }

    private void establishConnection(){
        try {
            mySocket = myServerSocket.accept();
            myBR = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
            myBW = new BufferedWriter(new OutputStreamWriter(mySocket.getOutputStream()));
            System.out.println("Crawler connected on:");
            System.out.println(mySocket.getInetAddress().toString() + ":" + mySocket.getPort());
            System.out.println();
            Logger.setToLog("Crawler connected on: " + mySocket.getInetAddress().toString() + ":" + mySocket.getPort());
        } catch (IOException ex) {
            Logger.setToErrorLog(ex.getMessage() + ". Can't use CrawlerConnector. Chek connection.");
        }
    }
}
