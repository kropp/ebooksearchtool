package org.ebooksearchtool.analyzer.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import org.ebooksearchtool.analyzer.io.Logger;

/**
 * @author Aleksey Podolskiy
 */

public class UIThread extends Thread{
    public UIThread(){
    }

    @Override
    public synchronized void run(){
        while(true){
            String input = "";
            Scanner scanner = new Scanner(System.in);
            while (true) {
                input = scanner.nextLine();
                if (input.contains("exit")) {
                    Logger.setToLog("Analyzer stoped by user.");
                    System.exit(0);
                }
            }
        }
    }
}
