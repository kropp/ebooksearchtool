package org.ebooksearchtool.analyzer.network;

import org.ebooksearchtool.analyzer.utils.NetUtils;
import java.net.*;
import java.io.*;
import org.ebooksearchtool.analyzer.io.*;

/**
 * @author Алексей
 */

public class ClientURLThread extends Thread{
    private URL myURL = null;

    public ClientURLThread(URL url){
        myURL = url;
    }

    @Override
    public void run(){
        try {
            URLConnection connection = myURL.openConnection();
            InputStream is;
            OutputStream os;

            is = connection.getInputStream();
            os = connection.getOutputStream();
            StringBuilder buffer = new StringBuilder();
            byte[] bytes = new byte[255];

            while(true){
                while(buffer.indexOf(";") == -1){
                    System.in.read(bytes);
                }
                NetUtils.sendMessage(os, buffer.toString());
                buffer = new StringBuilder(NetUtils.reciveMessage(is));
                System.out.print(buffer.toString());
                if(buffer.indexOf("quit") == -1){
                    break;
                }
            }
        } catch (IOException ex) {
            Logger.setToLog("ServerURLThread: " + ex.getMessage());
        }
    }
}
