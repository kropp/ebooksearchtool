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
                    case 'n':{
                        NetworkSocketIO.createServer(null, 9999);
                        NetworkSocketIO.createClient(null, 8000, 1000);
//                        ArrayList<Lexema> les = Lexema.convertToLexems("<h3>Book Summary</h3> <p>Merry Christmas, everyone! “Bah!” said Scrooge. “Humbug!” With those famous words unfolds a tale that renews the joy and caring that are Christmas. Whether we read it aloud with our family and friends or open the pages on a chill winter evening to savor the story in solitude, Charles Dickens’s A Christmas Carol is a very special holiday experience. It is the one book that every year will warm our hearts with favorite memories of Ebenezer Scrooge, Tiny Tim, Bob Cratchit, and the Ghosts of Christmas Past, Present, and Future — and will remind us with laughter and tears that the true Christmas spirit comes from giving with love.With a heartwarming account of Dickens’s first reading of the Carol, and a biographical sketch.</p></div>");
//
//                        epubAnnotationExtractor.extractAnnotation(les);
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
