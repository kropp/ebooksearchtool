package org.ebooksearchtool.analyzer.io;

import java.io.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import org.ebooksearchtool.analyzer.utils.AnalyzerProperties;

/**
 * @author Алексей
 */

public class Logger {

    public static synchronized void setToErrorLog(String message){
        RandomAccessFile log = null;
        try{
            try{
                makeDirectory();
                log = new RandomAccessFile(getErrorLogPath(), "rws");
                long length = log.length();
                log.readFully(new byte[(int)length]);
                log.writeBytes(getCurrentTime());
                log.writeBytes(AnalyzerProperties.getPropertie("systemSeparator"));
                log.writeBytes(message);
                log.writeBytes(AnalyzerProperties.getPropertie("systemSeparator"));
                log.writeBytes(AnalyzerProperties.getPropertie("systemSeparator"));
                } catch (IOException ex) {
                    System.out.print(ex.toString());
                }finally{
                    log.close();
                }
        }catch(IOException ex){
            System.out.println("Can't write to log. " + ex.getMessage());
        }
    }

     public static synchronized void setToLog(String message){
        RandomAccessFile log = null;
        try{
            try{
                makeDirectory();
                log = new RandomAccessFile(getLogPath(), "rws");
                long length = log.length();
                log.readFully(new byte[(int)length]);
                log.writeBytes(getCurrentTime());
                log.writeBytes(AnalyzerProperties.getPropertie("systemSeparator"));
                log.writeBytes(message);
                log.writeBytes(AnalyzerProperties.getPropertie("systemSeparator"));
                log.writeBytes(AnalyzerProperties.getPropertie("systemSeparator"));
                } catch (IOException ex) {
                    System.out.print(ex.toString());
                }finally{
                    log.close();
                }
        }catch(IOException ex){
            System.out.println("Can't write to log. " + ex.getMessage());
        }
    }


    private static String getCurrentDate(){
        GregorianCalendar calendar = new GregorianCalendar(TimeZone.getDefault());
        return format(calendar.get(Calendar.YEAR))
               + "_" + format(calendar.get(Calendar.MONTH) + 1)
               + "_" + format(calendar.get(Calendar.DATE));
    }

    private static String getCurrentTime(){
        GregorianCalendar calendar = new GregorianCalendar(TimeZone.getDefault());
        return format(calendar.get(Calendar.HOUR_OF_DAY))
               + ":" + format(calendar.get(Calendar.MINUTE))
               + ":" + format(calendar.get(Calendar.SECOND));
    }

    private static void makeDirectory(){
        File directory = new File(AnalyzerProperties.getPropertie("logDirectoryName"));
        if(!directory.exists()){
            directory.mkdir();
        }
    }

    private static String getErrorLogPath(){
        return AnalyzerProperties.getPropertie("logDirectoryName") +
                AnalyzerProperties.getPropertie("file.separator") +
                AnalyzerProperties.getPropertie("file.separator") +
                "errorlog" + getCurrentDate() + ".txt";
    }

    private static String getLogPath(){
        return AnalyzerProperties.getPropertie("logDirectoryName") +
                AnalyzerProperties.getPropertie("file.separator") +
                AnalyzerProperties.getPropertie("file.separator") +
                "log" + getCurrentDate() + ".txt";
    }

    private static String format(int i){
        if(i < 10){
            return "0" + i;
        }
        return i + "";
    }
}
