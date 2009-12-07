package org.ebooksearchtool.analyzer.network;

import java.util.List;
import org.ebooksearchtool.analyzer.algorithms.WholeParser;
import org.ebooksearchtool.analyzer.algorithms.WholeStringSimpleParser;
import org.ebooksearchtool.analyzer.io.Logger;
import org.ebooksearchtool.analyzer.model.Author;
import org.ebooksearchtool.analyzer.model.BookInfo;
import org.ebooksearchtool.analyzer.model.File;
import org.ebooksearchtool.analyzer.utils.BookInfoFormer;
import org.ebooksearchtool.analyzer.utils.AnalyzeUtils;
import org.ebooksearchtool.analyzer.utils.AnalyzerProperties;
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
                    printInfo(info);
                    //TODO: Добавить разбор ответов от сервера  
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
           System.out.print("    Link: " + files.get(i).getLink() +"; " );
           System.out.print("Size: " + files.get(i).getSize() +"; " );
           System.out.println("Type: " + files.get(i).getType());
           System.out.println("Book Cover: " + files.get(i).getImgLink());
       }
       System.out.println("Language: " + info.getLanguage());
       List<String> annotations = info.getAnnotations();
       length = annotations.size();
       System.out.println("Annotations: ");
       for (int i = 0; i < length; i++) {
           System.out.println(annotations.get(i));
       }
    }
}
