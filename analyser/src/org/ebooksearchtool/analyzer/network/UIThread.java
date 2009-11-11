package org.ebooksearchtool.analyzer.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.ebooksearchtool.analyzer.io.Logger;

/**
 * @author Aleksey Podolskiy
 */

public class UIThread extends Thread{
    public UIThread(){
    }

    @Override
    public synchronized void run(){
        try {
            while(true){
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                String str = br.readLine();
                if(str.contains("exit")){
                    Logger.setToLog("Analyzer stoped by user.");
                    System.exit(0);
                }
            }
        } catch (IOException ex) {
            Logger.setToErrorLog(ex.getMessage());
        }
    }
}
