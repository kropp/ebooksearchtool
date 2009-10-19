package org.ebooksearchtool.analyzer.io;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.ebooksearchtool.analyzer.utils.Properties;
import org.xml.sax.SAXException;

/**
 * @author Алексей
 */

public class PropertiesIO {
    public static void getPropertiesFromFile(String file) throws SAXException, ParserConfigurationException, IOException{
        SAXParserFactory saxFactory = SAXParserFactory.newInstance();
        SAXParser pars1 = saxFactory.newSAXParser();
        PropertiesHandler dh = new PropertiesHandler();
        pars1.parse(file, dh);
    }

    public static void writePropertiesToFile(String file){
        BufferedWriter bw = null;
        try{
            try {
                bw = new BufferedWriter(new FileWriter(file));
                bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                bw.newLine();
                bw.write("<root>");
                bw.newLine();
                bw.write("<field type=\"numberOfAnalyzerThreads\">" +
                        Properties.getPropertie("numberOfAnalyzerThreads") +
                        "</field>");
                bw.newLine();
                bw.write("<field type=\"logDirectoryName\">" +
                        Properties.getPropertie("logDirectoryName") +
                        "</field>");
                bw.newLine();
                bw.write("</root>");
            } catch (IOException ex) {
                Logger.setToLog(ex.getMessage());
            }finally{
                bw.close();
            }
        }catch(IOException ex){
            Logger.setToLog(ex.getMessage());
        }
    }
}
