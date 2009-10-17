package org.ebooksearchtool.analyzer.io;

import java.io.*;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

/**
 * @author Алексей
 */

public class Logger {

    public static void setToLog(String message){
        String s = "\\log\\log" + getCurrentDate() + ".txt";
        RandomAccessFile log = null;
        try{
            try{
                log = new RandomAccessFile(s, "rws");
                long length = log.length();
                log.readFully(new byte[(int)length]);
                log.writeBytes(getCurrentTime());
                log.writeBytes(System.getProperty("line.separator"));
                log.writeBytes(message);
                log.writeBytes(System.getProperty("line.separator"));
                log.writeBytes(System.getProperty("line.separator"));
                } catch (IOException ex) {
                    System.out.print(ex.toString());
                }finally{
                    log.close();
                }
        }catch(IOException ex){}
    }

    private static String getCurrentDate(){
        TimeZone zone = new SimpleTimeZone(3, "Moscow");
        GregorianCalendar calendar = new GregorianCalendar(zone);
        return calendar.get(Calendar.YEAR)
               + "_" + calendar.get(Calendar.MONTH)
               + "_" + calendar.get(Calendar.DATE);
    }

    private static String getCurrentTime(){
        TimeZone zone = new SimpleTimeZone(3, "Moscow");
        GregorianCalendar calendar = new GregorianCalendar(zone);
        return calendar.get(Calendar.HOUR_OF_DAY)
               + ":" + calendar.get(Calendar.MINUTE)
               + ":" + calendar.get(Calendar.SECOND);
    }
}
