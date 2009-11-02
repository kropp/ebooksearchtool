package org.ebooksearchtool.analyzer.utils;

/**
 * @author Алексей
 */

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.SortedMap;
import javax.xml.parsers.ParserConfigurationException;
import org.ebooksearchtool.analyzer.algorithms.subalgorithms.FormatExtractor;
import org.xml.sax.SAXException;
import org.ebooksearchtool.analyzer.io.Logger;
import org.ebooksearchtool.analyzer.io.PropertiesIO;
import org.ebooksearchtool.analyzer.model.Lexema;
import org.ebooksearchtool.analyzer.network.NetworkSocketIO;
import org.ebooksearchtool.analyzer.tests.TestBuilder;
import org.ebooksearchtool.analyzer.tests.Tester;
import org.ebooksearchtool.analyzer.tests.WholeStringSimpleTest;

public class ConsoleInputParameters {

    public static void switchMode(String[] args){
        if(args.length != 0){
        switch (args[0].charAt(1)){
            case 't':{
                switch (args[0].charAt(2)){
                    case 'a':{
                    try {
                        Tester.AuthorTest();
                    } catch (ParserConfigurationException ex) {
                        Logger.setToErrorLog(ex.getMessage());
                    } catch (SAXException ex) {
                        Logger.setToErrorLog(ex.getMessage());
                    } catch (IOException ex) {
                        Logger.setToErrorLog(ex.getMessage());
                    }
                       break;
                    }
                    case 'w':{
                        WholeStringSimpleTest.createTest(new File("urlexample.txt"));
                        break;
                    }
                    case 'n':{
                        FormatExtractor.extractFormat(Lexema.convertToLexems("<link src=\"http://www.getfreeebooks.com/wp-content/uploads/2009/It's.In.The.Music.pdf\" />"));
                        PropertiesIO.getPropertiesFromFile("analyzer.properties");
                        NetworkSocketIO.createServer("localhost",
                                AnalyzerProperties.getPropertieAsNumber("crawlerPort"));
                        NetworkSocketIO.createClient(AnalyzerProperties.getPropertie("serverAddress"),
                                AnalyzerProperties.getPropertieAsNumber("serverPort"),
                                AnalyzerProperties.getPropertieAsNumber("serverConnectionTimeout"));
                    }
                }
                break;
            }
            case 'b':{
                switch (args[0].charAt(2)){
                    case 'a':{
                    try {
                        TestBuilder.build(new File("munsey.xml"));
                    } catch (ParserConfigurationException ex) {
                        Logger.setToErrorLog(ex.getMessage());
                    } catch (SAXException ex) {
                        Logger.setToErrorLog(ex.getMessage());
                    } catch (IOException ex) {
                        Logger.setToErrorLog(ex.getMessage());
                    }
                       break;
                    }
                }
                break;
            }
            default:{}
        }
        }else{
            System.out.print("Wrong parameters");
        }
    }
}
