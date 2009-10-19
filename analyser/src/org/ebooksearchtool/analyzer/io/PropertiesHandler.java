package org.ebooksearchtool.analyzer.io;

/**
 * @author Алексей
 */

import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.ebooksearchtool.analyzer.utils.Properties;

public class PropertiesHandler extends DefaultHandler{

    private static String ourElementType = "";
    private static boolean ourRightElementFlag = false;

    public PropertiesHandler(){
        super();
    }

    @Override
    public void characters (char ch[], int start, int length) throws SAXException{
        if(ourRightElementFlag == true){
            Properties.setPropertie(ourElementType, new String(ch, start, length).trim());
            ourRightElementFlag = false;
        }
    }

    @Override
    public void endElement (String uri, String localName, String qName)
	throws SAXException
    {
        ourElementType = "";
        ourRightElementFlag = true;
    }

    @Override
    public void startElement (String uri, String localName,
			      String qName, Attributes attributes)
	throws SAXException
    {
        if(qName.equals("field")){
            ourElementType = attributes.getValue("type");
            ourRightElementFlag = true;
        }
    }

    @Override
    public void endDocument () throws SAXException {
        
    }
}
