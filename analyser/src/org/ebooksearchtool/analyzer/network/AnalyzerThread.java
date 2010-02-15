package org.ebooksearchtool.analyzer.network;

import org.ebooksearchtool.analyzer.algorithms.WholeParser;
import org.ebooksearchtool.analyzer.io.Logger;
import org.ebooksearchtool.analyzer.model.BookInfo;
import org.ebooksearchtool.analyzer.utils.NetUtils;

/**
 * @author Алексей
 */

public class AnalyzerThread extends Thread {

    private String myMessage;
    private final Object myLock;

    public AnalyzerThread(){
        myMessage = "";
        myLock = new Object();
    }

    @Override
    public synchronized void run(){
        synchronized(myLock){
            while(true){
                try {
                    while ("".equals(myMessage)) {
                        myLock.wait();
                    }
                    WholeParser ws = new WholeParser();
                    BookInfo info = ws.parse(myMessage);
                    info.printInfo();
                    NetUtils.sendBookInfo(info);
                    myMessage = "";
                } catch (InterruptedException ex) {
                    Logger.setToErrorLog(ex.getMessage() + ". Analyzer thread had been interrupted.");
                }
            }
        }
    }

    public boolean setMessage(String message){
        synchronized(myLock){
            if(myMessage.equals("")){
                myMessage = message;
                myLock.notify();
                return true;
            }
            return false;
        }
    }
}
