package org.ebooksearchtool.analyzer.tests;

/**
 * @author Алексей
 */

import java.util.ArrayList;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.ebooksearchtool.analyzer.algorithms.AuthorsSimpleParser;
import org.ebooksearchtool.analyzer.io.TestToFileWriter;

public class SimpleTestBuildHandler extends DefaultHandler{

    private static boolean rightElementFlag = false;
    private static ArrayList<ArrayList<String>> authors = new ArrayList<ArrayList<String>>();

    public SimpleTestBuildHandler(){
        super();
    }

    @Override
    public void characters (char ch[], int start, int length) throws SAXException{
        if(rightElementFlag == true){
            AuthorsSimpleParser parser = AuthorsSimpleParser.Instance();
            SimpleTestBuildHandler.authors.add(parser.parse(new String(ch, start, length).trim()));
            rightElementFlag = false;
        }
    }

    @Override
    public void endElement (String uri, String localName, String qName)
	throws SAXException
    {
        rightElementFlag = false;
    }
    
    @Override
    public void startElement (String uri, String localName,
			      String qName, Attributes attributes)
	throws SAXException
    {
        if(qName.equals("field")){
            String attr = attributes.getValue("name");
            if(attr != null && attr.equals("author")){
                rightElementFlag = true;
            }
        }
    }

    @Override
    public void endDocument () throws SAXException {
        TestToFileWriter.writeTestToFile(authors, "simpletest.tst");
    }
}
