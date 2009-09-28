package org.ebooksearchtool.analyzer.io;

import java.io.*;
import java.util.ArrayList;

/**
 * @author Алексей
 */

public class Serializator {

    public static boolean serialazeTest(ArrayList<ArrayList<String>> test, String file){
        ObjectOutputStream out = null;
        try{
            try{
                out = new ObjectOutputStream(new FileOutputStream(file));
                out.writeObject(test);
            }catch (IOException ex){
                Logger.setToLog("Serializator: " + ex.getMessage());
                return false;
            }finally{
                out.close();
            }
        }catch(IOException ex){
            Logger.setToLog("Serializator: " + ex.getMessage());
        }
        return true;
    }

    public static ArrayList<ArrayList<String>> deserialazeTest(String file){
        ObjectInputStream out = null;
        ArrayList<ArrayList<String>> data = null;
        try{
            try{
                out = new ObjectInputStream(new FileInputStream(file));

                try {
                    data = (ArrayList<ArrayList<String>>) out.readObject();
                } catch (ClassNotFoundException ex) {
                    Logger.setToLog("Serializator: " + ex.getMessage());
                }
            }catch (IOException ex){
                Logger.setToLog("Serializator: " + ex.getMessage());
            }finally{
                out.close();
            }
        }catch(IOException ex){
            Logger.setToLog("Serializator: " + ex.getMessage());
        }
        return data;
    }
}
