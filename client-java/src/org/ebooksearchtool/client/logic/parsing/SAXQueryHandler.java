package org.ebooksearchtool.client.logic.parsing;

import org.ebooksearchtool.client.model.settings.Settings;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/*
 * Date: 18.02.2010
 * Time: 22:14:19
 */
public class SAXQueryHandler extends DefaultHandler {

    private String mySearchLink;

    public SAXQueryHandler(){
    }

    @Override
    public void startDocument() throws SAXException
    {
    }
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
    {

        if ("link".equals(qName) && "search".equals(attributes.getValue("rel"))) {
            System.out.println(attributes.getValue("href"));
            mySearchLink = attributes.getValue("href");
        }else if("Url".equals(qName) && "application/atom+xml".equals(attributes.getValue("type"))){
            System.out.println(attributes.getValue("template"));
            mySearchLink = attributes.getValue("template");
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
