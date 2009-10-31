package org.ebooksearchtool.analyzer.utils;

/**
 * @author Алексей
 */

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.logging.Level;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import org.ebooksearchtool.analyzer.algorithms.AuthorsSimpleParser;
import org.ebooksearchtool.analyzer.algorithms.subalgorithms.epubAnnotationExtractor;
import org.ebooksearchtool.analyzer.io.Logger;
import org.ebooksearchtool.analyzer.io.PropertiesIO;
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
                        PropertiesIO.getPropertiesFromFile("analyzer.properties");
                        NetworkSocketIO.createServer(null, 9999);
                        NetworkSocketIO.createClient(null, 8000, 1000);
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
