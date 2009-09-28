package Logic.Parser;

import org.xml.sax.XMLReader;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.ParserConfigurationException;
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

    SAXParserFactory parserFactory = SAXParserFactory.newInstance();

    SAXParser parser;


    public Parser() throws SAXException, ParserConfigurationException {
        parser = parserFactory.newSAXParser();
    }

    public void parse(String fileName) throws IOException, SAXException {
        SAXHandler handler = new SAXHandler();
        parser.parse(new File(fileName), handler);

    }

}
