package org.ebooksearchtool.client.logic.parsing;

import org.ebooksearchtool.client.model.Data;
import org.ebooksearchtool.client.model.DataElement;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.lang.reflect.InvocationTargetException;

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

/*        for (int i = 0; i < 2; ++i){
            System.out.println(myBooks.getClass().getDeclaredFields()[i]);
        }
        for (int i = 0; i < 8; ++i){
            System.out.println(myBooks.getClass().getMethods()[i]);
        }
  */
    }
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException
    {
        if (myIsEntryTag){
            for(int i = 0; i < myTags.getTags().length; ++i){
                if(myTags.getTags()[i].getStatus()) {
                    if(myIsContinue){
                        try {
                            myBooks.getClass().getMethods()[i+2].invoke(myBooks, myBooks.getInfo().size()-1, myBooks.getInfo().get(myBooks.getInfo().size()-1).getClass().getMethods()[2*i].invoke(myBooks.getInfo().get(myBooks.getInfo().size()-1)) + new String(ch, start, length));
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }
                    }else{
                        try {
                            myBooks.getClass().getMethods()[i+2].invoke(myBooks, myBooks.getInfo().size()-1, new String(ch, start, length));
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }    
                        myIsContinue = true;
                    }
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
