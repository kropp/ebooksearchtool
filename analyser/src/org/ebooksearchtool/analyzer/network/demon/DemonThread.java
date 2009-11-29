package org.ebooksearchtool.analyzer.network.demon;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import org.ebooksearchtool.analyzer.io.Logger;
import org.ebooksearchtool.analyzer.utils.AnalyzerProperties;

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
        //TODO:Сделать возможным запуск демона во время работы программы.
        if(AnalyzerProperties.getPropertieAsBoolean("demon_is_enable")){
            URL address;
            Object waiter = new Object();
            Calendar calendar = new GregorianCalendar(TimeZone.getDefault());

            synchronized(waiter){
                while(true){
                    try{
                        if(!myTodayUpdateFlag){
                            while(calendar.get(Calendar.HOUR_OF_DAY) <
                                    AnalyzerProperties.getPropertieAsNumber("demonHourWhenRefresh")){
                                try {
                                    waiter.wait(AnalyzerProperties.getPropertieAsNumber("demonRepeatConditionsChekTime"));
                                } catch (InterruptedException ex) {
                                    Logger.setToErrorLog(ex.getMessage());
                                }
                            }
                            //TODO:Сейчас работает с локальным файлом, в итоге должен скачивать его. Доделать удаление фала, если он не докачан
    //                        try {
    //                            address = new URL("http://www.munseys.com/munsey.xml");
    //                            URLConnection connect = address.openConnection();
    //
    //                            OutputStream out = new FileOutputStream(new File("munsey.xml"));
    //                            long i = 0;
    //                            long end = connect.getContentLength();
    //                            if(end < 0){
    //                                throw new IOException("Connection faild");
    //                            }
    //                            Logger.setToLog("Demon file download started: " + address);
    //                            while (i<end){
    //                                int b = connect.getInputStream().read();
    //                                out.write(b);
    //                                i++;
    //                            }
    //                            out.close();
                                MunseyParser parser = new MunseyParser();
                                parser.parse("munsey.xml");
    //                        } catch (IOException ex) {
    //                            Logger.setToErrorLog(ex.getMessage());
    //                        }finally{
    //                            if(out != null){
    //                                out.close();
    //                            }
    //                        }
                        }else{
                            while(calendar.get(Calendar.HOUR_OF_DAY) > 1){
                                try {
                                    waiter.wait(AnalyzerProperties.getPropertieAsNumber("demonRepeatConditionsChekTime"));
                                } catch (InterruptedException ex) {
                                    Logger.setToErrorLog(ex.getMessage());
                                }
                            }
                            myTodayUpdateFlag = false;
                        }
                    }catch(NullPointerException ex){
                            Logger.setToErrorLog(ex.getMessage());
                    }
                }
            }
        }
    }
}
