package org.ebooksearchtool.client.logic.parsing;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;

/*
 * Date: 18.02.2010
 * Time: 22:14:19
 */
public class SAXQueryHandler extends DefaultHandler {

    private String mySearchLink;
    boolean myIsSearched = false;

    public SAXQueryHandler(){
    }

    @Override
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        return new org.xml.sax.InputSource(new java.io.StringReader(""));
    }

    @Override
    public void startDocument() throws SAXException
    {
    }
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
    {

        if (!myIsSearched) {
            if ("link".equals(qName) && "search".equals(attributes.getValue("rel"))) {
                mySearchLink = attributes.getValue("href");
                myIsSearched = true;
            } else if ("Url".equals(qName) && "application/atom+xml".equals(attributes.getValue("type"))) {
                mySearchLink = attributes.getValue("template");
                myIsSearched = true;
            }
        }

    }
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException
    {



    }
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException
    {
    }
    @Override
    public void endDocument() throws SAXException
    {
    }

    public String getSearchLink() {
        return mySearchLink;
    }
}
