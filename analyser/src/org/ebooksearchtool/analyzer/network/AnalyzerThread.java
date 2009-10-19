package org.ebooksearchtool.analyzer.network;

import java.util.List;
import org.ebooksearchtool.analyzer.algorithms.WholeStringSimpleParser;
import org.ebooksearchtool.analyzer.io.Logger;
import org.ebooksearchtool.analyzer.model.Author;
import org.ebooksearchtool.analyzer.model.BookInfo;
import org.ebooksearchtool.analyzer.model.File;
import org.ebooksearchtool.analyzer.utils.Messages;

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
                    myLock.wait();
                    WholeStringSimpleParser ws = new WholeStringSimpleParser();
                    BookInfo info = ws.parse(myMessage);
                    printInfo(info);
                    //TODO: Добавить разбор ответов от сервера  
                    String message = ClientSocketThread.sendRequest(Messages.formBookInfo(info));
                    System.out.println(message);
                    myMessage = "";
                } catch (InterruptedException ex) {
                    Logger.setToLog(ex.getMessage());
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

    private void printInfo(BookInfo info){
       System.out.println("Title: " + info.getTitle());
       List<Author> authors = info.getAuthors();
       int length = authors.size();
       System.out.println("Authors: ");
       for (int i = 0; i < length; i++) {
           System.out.println("    " + authors.get(i).getName());
       }
       List<File> files = info.getFiles();
       length = files.size();
       System.out.println("Files: ");
       for (int i = 0; i < length; i++) {
           System.out.println("    " + files.get(i).getLink());
       }
       System.out.println("Language: " + info.getLanguage());
   }
}
