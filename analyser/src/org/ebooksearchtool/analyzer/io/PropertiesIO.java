package org.ebooksearchtool.analyzer.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import org.ebooksearchtool.analyzer.utils.AnalyzerProperties;

/**
 * @author Алексей
 */

public class PropertiesIO {
    public static void getPropertiesFromFile(String file){
        try {
            //TODO: В перспективе надо брать название property файла из параметров командной строки
            Properties prop = new Properties();
            prop.load(new BufferedReader(new FileReader(file)));
            Set<Entry<Object, Object>> entrys = prop.entrySet();
            for (Entry<Object, Object> entry : entrys) {
                AnalyzerProperties.setPropertie((String) entry.getKey(), (String) entry.getValue());
            }
        } catch (IOException ex) {
            Logger.setToErrorLog(ex.getMessage());
        }
    }

    public static void writePropertiesToFile(String file){
        BufferedWriter bw = null;
        try{
            try {
                bw = new BufferedWriter(new FileWriter(file));

                HashMap<String,String> properties = AnalyzerProperties.getProperties();
                Set<String> keys = properties.keySet();
                for(String key : keys){
                    if(!key.contains("system")){
                        bw.write(key + "=" + properties.get(key));
                        bw.newLine();
                    }
                }
            } catch (IOException ex) {
                Logger.setToErrorLog(ex.getMessage());
            }finally{
                bw.close();
            }
        }catch(IOException ex){
            Logger.setToErrorLog(ex.getMessage());
        }
    }
}
