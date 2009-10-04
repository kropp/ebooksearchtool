package org.ebooksearchtool.analyzer.tests;

/**
 * @author Алексей
 */

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;
import org.ebooksearchtool.analyzer.algorithms.IParser;
import org.ebooksearchtool.analyzer.io.TestToFileWriter;

public class Tester {

    public static void Test(IParser parser) throws ParserConfigurationException, SAXException, IOException{
        ArrayList<ArrayList<String>> test = TestToFileWriter.readTestFromFile("simpletest.tst");
        
        BufferedWriter out = new BufferedWriter(new FileWriter("testresult.txt"));
        boolean testPassedFlag = true;
        
        SAXParserFactory factory1 = SAXParserFactory.newInstance();
        SAXParser pars1 = factory1.newSAXParser();
        SimpleAuthorsHandler dh = new SimpleAuthorsHandler();
        pars1.parse("munsey.xml", dh);
        ArrayList<ArrayList<String>> parsed = TestToFileWriter.readTestFromFile("authors.out");

        int length = parsed.size();
        int lengthOfTest = test.size();
        if(length != lengthOfTest){
            out.write("Test and example have different length. Please rebuild the test or example isn't correct");
            return;
        }

        for (int i = 0; i < length; i++) {
            ArrayList<String> testElem = test.get(i);
            ArrayList<String> parsedElem = parsed.get(i);
            if(!testElem.equals(parsedElem)){
                out.write("Need: " + testElem + "; is: " + parsedElem);
                out.newLine();
                testPassedFlag = false;
            }
        }

        if(testPassedFlag){
            out.write("Test passed.");
        }

        out.close();
    }
}
