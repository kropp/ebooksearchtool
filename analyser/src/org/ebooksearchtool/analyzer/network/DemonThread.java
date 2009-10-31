package org.ebooksearchtool.analyzer.network;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import org.ebooksearchtool.analyzer.algorithms.MunseyParser;
import org.ebooksearchtool.analyzer.io.Logger;

/**
 * @author Алексей
 */

public class DemonThread extends Thread{

    //TODO:Обрабатывать список файлов
    private List<String> myFilesToScan;
    private boolean myTodayUpdateFlag;

    public DemonThread(){
        myFilesToScan = new ArrayList<String>();
        myTodayUpdateFlag = false;
    }

    @Override
    public synchronized void run(){
        URL address;
        Object waiter = new Object();
        Calendar calendar = new GregorianCalendar(TimeZone.getDefault());

        while(true){
            if(!myTodayUpdateFlag){
                while(calendar.get(Calendar.HOUR_OF_DAY) > 15){
                    try {
                        waiter.wait(3600000);
                    } catch (InterruptedException ex) {
                        Logger.setToErrorLog(ex.getMessage());
                    }
                }
                try {
                    address = new URL("http://www.munseys.com/munsey.xml");
                    URLConnection connect = address.openConnection();

                    StringBuilder sb = new StringBuilder();
                    int currPosition = 0;
                    int end = connect.getContentLength();
                    InputStream input = connect.getInputStream();
                    while (currPosition != -1 && currPosition < end){
                        sb.append(input.read());
                        ++currPosition;
                    }
                    MunseyParser parser = new MunseyParser();
                    parser.parse(sb.toString());
                } catch (IOException ex) {
                    Logger.setToErrorLog(ex.getMessage());
                }
            }else{
                while(calendar.get(Calendar.HOUR_OF_DAY) > 1){
                    try {
                        waiter.wait(3600000);
                    } catch (InterruptedException ex) {
                        Logger.setToErrorLog(ex.getMessage());
                    }
                }
                myTodayUpdateFlag = false;
            }
        }

    }
}
