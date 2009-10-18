package org.ebooksearchtool.analyzer.io;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import org.ebooksearchtool.analyzer.model.Author;

/**
 * @author Алексей
 */

public class TestToFileWriter {

    public static boolean writeTestToFile(List<ArrayList<Author>> test, String file){
        BufferedWriter out = null;
        try{
            try{
                out = new BufferedWriter(new FileWriter(file));
                int length = test.size();
                for (int i = 0; i < length; i++) {
                    List<Author> temp = test.get(i);
                    int sizeOfSubArray = temp.size();
                    for (int j = 0; j < sizeOfSubArray; j++) {
                        out.write((String)temp.get(j).getName());
                        out.newLine();
                    }
                    out.write("    !Next book!    ");
                    out.newLine();
                }
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

    public static List<ArrayList<Author>> readTestFromFile(String file){
        BufferedReader input = null;
        ArrayList<ArrayList<Author>> data = new ArrayList<ArrayList<Author>>();
        try{
            try{
                input = new BufferedReader(new FileReader(file));
                String temp = input.readLine();
                ArrayList<Author> subArray = new ArrayList<Author>();
                while(temp != null){
                    while(temp != null && temp.equals("    !Next book!    ")){
                        subArray.add(new Author(temp));
                        temp = input.readLine();
                    }
                    if(temp == null){
                        break;
                    }
                    data.add(subArray);
                    subArray.clear();
                    temp = input.readLine();
                }
               
            }catch (IOException ex){
                Logger.setToLog("Serializator: " + ex.getMessage());
            }finally{
                input.close();
            }
        }catch(IOException ex){
            Logger.setToLog("Serializator: " + ex.getMessage());
        }
        return data;
    }
}
