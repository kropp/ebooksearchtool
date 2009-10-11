package org.ebooksearchtool.analyzer.network;

import org.ebooksearchtool.analyzer.utils.NetUtils;
import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Алексей
 */

public class ClientSocketThread extends Thread{

    private Socket mySocket;

    public ClientSocketThread(Socket sSocket){
        mySocket = sSocket;
    }

    @Override
    public synchronized void run(){
        BufferedReader br = null;
        BufferedWriter bw = null;
        try{
            try {
                br = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
                bw = new BufferedWriter(new OutputStreamWriter(mySocket.getOutputStream()));
                byte[] bytes = new byte[255];
                StringBuilder buffer = new StringBuilder();

                System.out.print(mySocket.getPort());

                while(true){
                    while(buffer.indexOf(";") == -1){
                        System.in.read(bytes);
                        buffer.append(NetUtils.convertBytesToString(bytes));
                    }
                    NetUtils.sendMessage(bw, buffer.toString().trim());
                    buffer = new StringBuilder(NetUtils.reciveMessage(br));
                    System.out.print(buffer.toString());
                    if(buffer.indexOf("quit") != -1){
                        break;
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(ClientSocketThread.class.getName()).log(Level.SEVERE, null, ex);
            }finally{
                br.close();
                bw.close();
                mySocket.close();
            }
        }catch(IOException ex){
            Logger.getLogger(ClientSocketThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
