package org.ebooksearchtool.client.tests;

import org.ebooksearchtool.client.logic.parsing.Parser;
import org.ebooksearchtool.client.logic.parsing.SAXHandler;
import org.ebooksearchtool.client.model.QueryAnswer;
import org.ebooksearchtool.client.model.books.Data;
import org.ebooksearchtool.client.utils.XMLBuilder;
import org.ebooksearchtool.client.view.Window;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Виктор
 * Date: 07.02.2010
 * Time: 21:00:24
 * To change this template use File | Settings | File Templates.
 */
public class SAXParserTest {

    public static void test(){

        String[] testFiles = new String[] {"pushkin.xml", "tolstoy.xml", "hawking.xml", "dikkens.xml", "doyle.xml"};

        for(int i = 0; i < 5; ++i){
            QueryAnswer data = new QueryAnswer(new Data());
            Parser parser = null;
            try {
                parser = new Parser();
                SAXHandler handler = new SAXHandler(data, new Window());
                parser.parse("src/testFiles/" + testFiles[i], handler);

                XMLBuilder builder = new XMLBuilder();
                builder.makeXML(data.getData(), "src/testFiles/t_" + testFiles[i]);

                StringBuffer fileData = new StringBuffer(1000);
                BufferedReader reader = new BufferedReader(new FileReader("src/testFiles/" + testFiles[i]));
                char[] buf = new char[1024];
                int numRead=0;
                while((numRead=reader.read(buf)) != -1){
                    String readData = String.valueOf(buf, 0, numRead);
                    fileData.append(readData);
                    buf = new char[1024];
                }
                reader.close();

                StringBuffer testFileData = new StringBuffer(1000);
                reader = new BufferedReader(new FileReader("src/testFiles/t_" + testFiles[i]));
                char[] testBuf = new char[1024];
                numRead=0;
                while((numRead=reader.read(testBuf)) != -1){
                    String readData = String.valueOf(testBuf, 0, numRead);
                    testFileData.append(readData);
                    testBuf = new char[1024];
                }
                reader.close();

                if(!fileData.toString().equals(testFileData.toString())){
                    System.out.println("not equal in test " + testFiles[i]);
                    System.exit(1);
                }

            } catch (SAXException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (ParserConfigurationException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

        }

    }

}
