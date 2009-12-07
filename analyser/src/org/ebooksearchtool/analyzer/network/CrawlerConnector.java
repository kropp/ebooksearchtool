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

    public CrawlerConnector(ServerSocket sSocket){
        myServerSocket = sSocket;
        myThreads = new ArrayList<AnalyzerThread>();
        for (int i = 0; i < AnalyzerProperties.getPropertieAsNumber("numberOfAnalyzerThreads"); i++) {
            myThreads.add(new AnalyzerThread());
        }
        for (int i = 0; i < AnalyzerProperties.getPropertieAsNumber("numberOfAnalyzerThreads"); i++) {
            myThreads.get(i).start();
        }
    }

    @Override
    public synchronized void run(){
        BufferedReader br = null;
        BufferedWriter bw = null;
        boolean requestToAnalyzeFlag = false;
        int requestToAnalyzeCount = 0;
        try{
            try {
            DemonThread dt = new DemonThread();
            dt.start();

            mySocket = myServerSocket.accept();
            br = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
            bw = new BufferedWriter(new OutputStreamWriter(mySocket.getOutputStream()));
            String buffer = "";

            System.out.println("Crawler connected on:");
            System.out.println(mySocket.getInetAddress().toString() + ":" + mySocket.getPort());
            System.out.println();
            Logger.setToLog("Crawler connected on: " + mySocket.getInetAddress().toString() + ":" + mySocket.getPort());

            while(true){
                buffer = NetUtils.reciveCrawlerMessage(br);
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
        } catch (IOException ex) {
            Logger.setToLog(ex.getMessage() + ". Can't use CrawlerConnector. Chek connection.");
        }finally{
            br.close();
            bw.close();
            mySocket.close();
            myServerSocket.close();
            //TODO:Переделать выход из Analyzera
            System.exit(0);
        }
        }catch(IOException ex){
            Logger.setToLog(ex.getMessage() + ". Error in closing of corrupted crawler connection.");
            System.exit(0);
        }
    }
}
