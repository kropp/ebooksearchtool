package org.ebooksearchtool.analyzer.utils;

/**
 * @author Алексей
 */

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import org.ebooksearchtool.analyzer.algorithms.AuthorsSimpleParser;
import org.ebooksearchtool.analyzer.io.Logger;
import org.ebooksearchtool.analyzer.tests.TestBuilder;
import org.ebooksearchtool.analyzer.tests.Tester;
import org.ebooksearchtool.analyzer.tests.WholeStringSimpleTest;

public class ConsoleOutput {
    public static void switchMode(String[] args){
        if(args.length != 0){
        switch (args[0].charAt(1)){
            case 't':{
                switch (args[0].charAt(2)){
                    case 'a':{
                    try {
                        Tester.Test(AuthorsSimpleParser.Instance());
                    } catch (ParserConfigurationException ex) {
                        Logger.setToLog(ex.getMessage());
                    } catch (SAXException ex) {
                        Logger.setToLog(ex.getMessage());
                    } catch (IOException ex) {
                        Logger.setToLog(ex.getMessage());
                    }
                       break;
                    }
                    case 'w':{
                        WholeStringSimpleTest.createTest(new File("urlexample.txt"));
                        break;
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
                        Logger.setToLog(ex.getMessage());
                    } catch (SAXException ex) {
                        Logger.setToLog(ex.getMessage());
                    } catch (IOException ex) {
                        Logger.setToLog(ex.getMessage());
                    }
                       break;
                    }
                }
                break;
            }
        }
        }else{
            System.out.print("Wrong parameters");
        }
    }
}