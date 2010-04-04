package org.ebooksearchtool.client.logic.parsing;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.File;

/**
 * Date: 04.04.2010
 * Time: 20:58:08
 * To change this template use File | Settings | File Templates.
 */
public class SAXClearHandler extends DefaultHandler{

    boolean myIsImage;

    @Override
    public void startDocument() {
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes){
        myIsImage = "title".equals(qName);
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        if (myIsImage){
    		String name = new String(ch, start, length);
            File file = new File("images" + File.separatorChar + name.hashCode() + ".jpg");
            file.delete();
    	}
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        myIsImage = !"title".equals(qName);
    }

    @Override
    public void endDocument() throws SAXException {
    }

}
