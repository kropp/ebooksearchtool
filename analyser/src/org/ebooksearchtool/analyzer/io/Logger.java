package org.ebooksearchtool.analyzer.io;

import java.io.*;

/**
 * @author Алексей
 */

public class Logger {

    //TODO: Изменить логгер так, что бы он не пререписывал лог каждый раз заново
    public static void setToLog(String message){
        BufferedWriter out = null;
        try{
            try{
                out = new BufferedWriter(new FileWriter("log.txt"));
                out.write(message);
                out.newLine();
            }catch (IOException ex){

            }finally{
                out.close();
            }
        }catch(IOException ex){}
    }
}
