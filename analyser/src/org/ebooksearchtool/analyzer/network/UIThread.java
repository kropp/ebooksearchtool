package org.ebooksearchtool.analyzer.network;

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
                    System.out.println("Analyzer stoped by user.");
                    System.exit(0);
                }
            }
        }
    }
}
