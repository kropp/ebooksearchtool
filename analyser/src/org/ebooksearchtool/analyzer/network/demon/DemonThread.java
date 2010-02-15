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
import org.ebooksearchtool.analyzer.utils.NetUtils;

/**
 * @author Алексей
 */
public class DemonThread extends Thread {

    //TODO:Обрабатывать список файлов
    private List<String> myFilesToScan;
    private boolean myTodayUpdateFlag;

    public DemonThread() {
        myFilesToScan = new ArrayList<String>();
        myTodayUpdateFlag = false;
    }

    @Override
    public synchronized void run() {
        //TODO:Сделать возможным запуск демона во время работы программы.
        if (AnalyzerProperties.getPropertieAsBoolean("demon_is_enable")) {
            URL address;
            Object waiter = new Object();
            Calendar calendar = new GregorianCalendar(TimeZone.getDefault());

            synchronized (waiter) {
                while (true) {
                    if (!myTodayUpdateFlag) {
                        while (calendar.get(Calendar.HOUR_OF_DAY) <
                                AnalyzerProperties.getPropertieAsNumber("demon_refresh_time")) {
                            try {
                                waiter.wait(AnalyzerProperties.getPropertieAsNumber("demon_refresh_timeout"));
                            } catch (InterruptedException ex) {
                                Logger.setToErrorLog(ex.getMessage());
                            }
                        }
                        OutputStream out = null;
                        File munsey = null;
                        try {
                            try {
                                munsey = new File("munsey.xml");
                                if (munsey.createNewFile()) {
                                    address = new URL("http://www.munseys.com/munsey.xml");
                                    URLConnection connect = address.openConnection(NetUtils.proxyInit());

                                    out = new FileOutputStream(munsey);
                                    long i = 0;
                                    long end = connect.getContentLength();
                                    if (end < 0) {
                                        throw new IOException("Connection faild");
                                    }
                                    Logger.setToLog("Demon file download started: " + address);
                                    while (i < end) {
                                        int b = connect.getInputStream().read();
                                        out.write(b);
                                        i++;
                                    }
                                    out.close();
                                }
                                MunseyParser parser = new MunseyParser();
                                parser.parse("munsey.xml");
                                myTodayUpdateFlag = true;
                            } catch (IOException ex) {
                                Logger.setToErrorLog(ex.getMessage() + ". " +
                                        "Can't connect to " +
                                        "URL distanation or write to file in " +
                                        DemonThread.class.getName() + " class.");
                                munsey.delete();
                            } finally {
                                if (out != null) {
                                    out.close();
                                }
                            }
                        } catch (IOException ex) {
                            //This catch only for compilator tranquillity. Exception never thrown.
                        }
                    } else {
                        while (calendar.get(Calendar.HOUR_OF_DAY) > 1) {
                            try {
                                waiter.wait(AnalyzerProperties.getPropertieAsNumber("demon_refresh_timeout"));
                            } catch (InterruptedException ex) {
                                Logger.setToErrorLog(ex.getMessage() + ". Demon thread was interrupted.");
                            }
                        }
                        myTodayUpdateFlag = false;
                    }
                }
            }
        }
    }
}
