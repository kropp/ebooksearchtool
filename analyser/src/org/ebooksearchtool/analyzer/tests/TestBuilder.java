package org.ebooksearchtool.analyzer.tests;

/**
 * @author Алексей
 */

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;
public class TestBuilder {

    public static void build(File input) throws ParserConfigurationException, SAXException, IOException{
        SAXParserFactory factory1 = SAXParserFactory.newInstance();
        SAXParser pars1 = factory1.newSAXParser();
        SimpleTestBuildHandler dh = new SimpleTestBuildHandler();
        pars1.parse(input, dh);
    }
}
