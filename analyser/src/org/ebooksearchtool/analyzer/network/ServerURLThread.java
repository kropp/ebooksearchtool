package org.ebooksearchtool.analyzer.network;

import org.ebooksearchtool.analyzer.utils.NetUtils;
import java.io.*;
import java.net.*;
import org.ebooksearchtool.analyzer.io.*;

/**
 * @author Алексей
 */

public class ServerURLThread extends Thread{
    
    private URL myURL = null;
    
    public ServerURLThread(URL url){
        myURL = url;
    }
    
    @Override
    public void run(){
        try {
            URLConnection connection = myURL.openConnection();
            BufferedReader br = null;
            BufferedWriter bw = null;

            br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
            String buffer = "";

            while(true){
                buffer = NetUtils.reciveMessage(br);
                if(buffer.length() > 3){
                    buffer = buffer.substring(1, buffer.length() - 2);
                }
                //bw.write(NetUtils.convertToByteArray(buffer));
                if(buffer.indexOf("quit") != -1){
                    break;
                }
            }
        } catch (IOException ex) {
            Logger.setToLog("ServerURLThread: " + ex.getMessage());
        }
    }
}
