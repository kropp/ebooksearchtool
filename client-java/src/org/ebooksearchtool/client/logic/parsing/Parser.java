package org.ebooksearchtool.client.logic.parsing;

import org.ebooksearchtool.client.model.Data;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 22.09.2009
 * Time: 20:42:27
 * To change this template use File | Settings | File Templates.
 */
public class Parser {

    SAXParser myParser;


    public Parser() throws SAXException, ParserConfigurationException {
        SAXParserFactory parserFactory = SAXParserFactory.newInstance();
        myParser = parserFactory.newSAXParser();
    }

    public void parse(String fileName, DefaultHandler handler) throws IOException, SAXException {
        myParser.parse(new File(fileName), handler);
                           

    }

}
