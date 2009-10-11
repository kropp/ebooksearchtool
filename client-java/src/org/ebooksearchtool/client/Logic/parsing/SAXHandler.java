package org.ebooksearchtool.client.Logic.parsing;

import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.SAXException;

import org.xml.sax.Attributes;

import org.ebooksearchtool.client.Model.Data;
import org.ebooksearchtool.client.Model.DataElement;
import org.ebooksearchtool.client.Logic.parsing.*;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 25.09.2009
 * Time: 13:25:52
 * To change this template use File | Settings | File Templates.
 */
public class SAXHandler extends DefaultHandler{

    private Data myBooks;
    private boolean myIsEntryTag = false;
    private BookTags myTags = new BookTags();
    private boolean myIsContinue = false;           //Helps with parsing non-ASCII characters

    public SAXHandler(Data Books){

        myBooks = Books;

    }

    public Data getBooks()
    {
        return myBooks;
    }
    @Override
    public void startDocument() throws SAXException
    {
    }
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
    {
        if("entry".equals(qName)){
            myIsEntryTag = true;
            myBooks.addElement(new DataElement());
        }

        for(int i = 0; i < myTags.getTags().length; ++i){
            myTags.getTags()[i].setStatus(myTags.getTags()[i].getName().equals(qName));
        }
    }
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException
    {
        if (myIsEntryTag){
            for(int i = 0; i < myTags.getTags().length; ++i){
                if(myTags.getTags()[i].getStatus()) {
                    if(myIsContinue){
                        myBooks.setInfo(myBooks.getInfo().size()-1, i, myBooks.getInfo().get(myBooks.getInfo().size() - 1).getFields()[i] + new String(ch, start, length));
                    }else{
                        myBooks.setInfo(myBooks.getInfo().size()-1, i, new String(ch, start, length));
                        myIsContinue = true;
                    }
                 //   System.out.println(ch);
                 //   System.exit(1);
                }
            }
        }
    }
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException
    {
        if("entry".equals(qName)){
            myIsEntryTag = false;
        }
        for(int i = 0; i < myTags.getTags().length; ++i){
            myTags.getTags()[i].setStatus(false);
            myIsContinue = false;
        }
    }
    @Override
    public void endDocument() throws SAXException
    {
    }

}
