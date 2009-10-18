package org.ebooksearchtool.analyzer.network;

import org.ebooksearchtool.analyzer.utils.NetUtils;
import org.ebooksearchtool.analyzer.utils.Messages;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.ebooksearchtool.analyzer.algorithms.WholeStringSimpleParser;
import org.ebooksearchtool.analyzer.model.BookInfo;

/**
 * @author Алексей
 */

public class ServerSocketThread extends Thread{

    private ServerSocket myServerSocket = null;
    private Socket mySocket = null;

    public ServerSocketThread(ServerSocket sSocket){
        myServerSocket = sSocket;
    }

    @Override
    public synchronized void run(){
        BufferedReader br = null;
        BufferedWriter bw = null;
        try{
            try {
            mySocket = myServerSocket.accept();
            br = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
            bw = new BufferedWriter(new OutputStreamWriter(mySocket.getOutputStream()));
            String buffer = "";

                System.out.print(mySocket.getPort());

            while(true){
                buffer = NetUtils.reciveMessage(br);
                WholeStringSimpleParser ws = new WholeStringSimpleParser();
                BookInfo info = ws.parse(buffer);
                printInfo(info);
                String message = ClientSocketThread.sendRequest(info.getBookInfo());
                System.out.println(message);
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

   private void printInfo(BookInfo info){
       System.out.println("Title: " + info.getTitle());
       System.out.println("Authors: " + info.getAuthors());
       System.out.println("Files: " + info.getFiles());
       System.out.println("Language: " + info.getLanguage());
   }
}
