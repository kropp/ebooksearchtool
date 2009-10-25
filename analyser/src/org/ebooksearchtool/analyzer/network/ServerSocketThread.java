package org.ebooksearchtool.analyzer.network;

import org.ebooksearchtool.analyzer.utils.NetUtils;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.ebooksearchtool.analyzer.utils.Properties;

/**
 * @author Алексей
 */

public class ServerSocketThread extends Thread{

    private ServerSocket myServerSocket = null;
    private Socket mySocket = null;
    private List<AnalyzerThread> myThreads = null;

    public ServerSocketThread(ServerSocket sSocket){
        myServerSocket = sSocket;
        myThreads = new ArrayList<AnalyzerThread>();
        for (int i = 0; i < Integer.parseInt(Properties.getPropertie("numberOfAnalyzerThreads")); i++) {
            myThreads.add(new AnalyzerThread());
        }
        for (int i = 0; i < Integer.parseInt(Properties.getPropertie("numberOfAnalyzerThreads")); i++) {
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

            System.out.print(mySocket.getPort());

            while(true){
                buffer = NetUtils.reciveMessage(br);
                //Нужно только если тестить//System.out.println(buffer);
                while(!requestToAnalyzeFlag){
                    if(requestToAnalyzeCount == 10){
                        requestToAnalyzeCount = 0;
                    }
                    requestToAnalyzeFlag = myThreads.get(requestToAnalyzeCount).setMessage(buffer);
                    requestToAnalyzeCount++;
                }
                requestToAnalyzeFlag = false;
                //TODO:Разобраться с вылетом сервера
//                if(buffer.indexOf("quit") != -1){
//                    break;
//                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ServerSocketThread.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            br.close();
            bw.close();
            mySocket.close();
            myServerSocket.close();
        }
        }catch(IOException ex){
            Logger.getLogger(ServerSocketThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
