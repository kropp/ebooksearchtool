package org.ebooksearchtool.client.logic.parsing;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Date: 22.09.2009
 * Time: 20:42:27
 * To change this template use File | Settings | File Templates.
 */
public class Parser{

    SAXParser myParser;


    public Parser() throws SAXException, ParserConfigurationException {
        SAXParserFactory parserFactory = SAXParserFactory.newInstance();
        parserFactory.setNamespaceAware(true);
        myParser = parserFactory.newSAXParser();
    }

    public void parse(String fileName, DefaultHandler handler) throws IOException, SAXException {

        myParser.parse(new File(fileName), handler);

    }

    public void parse(InputStream is, DefaultHandler handler) throws IOException, SAXException {

        myParser.parse(is, handler);

    }

}
