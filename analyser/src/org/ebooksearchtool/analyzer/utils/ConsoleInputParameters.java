package org.ebooksearchtool.analyzer.utils;

/**
 * @author Алексей
 */

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import org.ebooksearchtool.analyzer.io.Logger;
import org.ebooksearchtool.analyzer.io.PropertiesIO;
import org.ebooksearchtool.analyzer.network.NetworkInitializator;
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
                        if(args.length > 1){
                            switch(args[1].charAt(1)){
                                case 'p': {
                                    PropertiesIO.getPropertiesFromFile(args[2]);
                                }
                            }
                        }
                        WholeStringSimpleTest.createTest(new File("urlexample.txt"));
                        break;
                    }
                    case 'n':{
                        if(args.length > 1){
                            switch(args[1].charAt(1)){
                                case 'p': {
                                    PropertiesIO.getPropertiesFromFile(args[2]);
                                }
                            }
                        }
                        NetworkInitializator.createCrawlerConnector("localhost",
                                AnalyzerProperties.getPropertieAsNumber("crawler_port"));
                        NetworkInitializator.createServerConnector(AnalyzerProperties.getPropertie("server_address"),
                                AnalyzerProperties.getPropertieAsNumber("server_port"),
                                AnalyzerProperties.getPropertieAsNumber("server_timeout"));
                        NetworkInitializator.createUI();
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
            case 'p':{
                if(args.length > 1){
                    PropertiesIO.getPropertiesFromFile(args[1]);
                }
                String[] arr = new String[1];
                arr[0] = "-tn";
                org.ebooksearchtool.analyzer.utils.ConsoleInputParameters.switchMode(arr);
            }
            default:{}
        }
        }else{
            String[] arr = new String[3];
            arr[0] = "-tn";
            arr[1] = "-p";
            arr[2] = "analyzer.properties";
            org.ebooksearchtool.analyzer.utils.ConsoleInputParameters.switchMode(arr);
        }
    }
}
