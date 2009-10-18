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
import org.ebooksearchtool.analyzer.model.Author;

public class SimpleTestBuildHandler extends DefaultHandler{

    private static boolean ourRightElementFlag = false;
    private static ArrayList<ArrayList<Author>> ourAuthors = new ArrayList<ArrayList<Author>>();

    public SimpleTestBuildHandler(){
        super();
    }

    @Override
    public void characters (char ch[], int start, int length) throws SAXException{
        if(ourRightElementFlag == true){
            AuthorsSimpleParser parser = AuthorsSimpleParser.Instance();
            SimpleTestBuildHandler.ourAuthors.add(parser.parse(new String(ch, start, length).trim()));
            ourRightElementFlag = false;
        }
    }

    @Override
    public void endElement (String uri, String localName, String qName)
	throws SAXException
    {
        ourRightElementFlag = false;
    }
    
    @Override
    public void startElement (String uri, String localName,
			      String qName, Attributes attributes)
	throws SAXException
    {
        if(qName.equals("field")){
            String attr = attributes.getValue("name");
            if(attr != null && attr.equals("author")){
                ourRightElementFlag = true;
            }
        }
    }

    @Override
    public void endDocument () throws SAXException {
        TestToFileWriter.writeTestToFile(ourAuthors, "simpletest.tst");
    }
}
