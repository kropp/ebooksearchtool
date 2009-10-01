package Logic.Parser;

import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.SAXException;

import org.xml.sax.Attributes;
import java.util.ArrayList;
import java.util.Vector;

import Model.Data;
import Model.DataElement;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 25.09.2009
 * Time: 13:25:52
 * To change this template use File | Settings | File Templates.
 */
public class SAXHandler extends DefaultHandler{

    private Data books;
    private boolean isEntryTag = false;
    private BookTags tags = new BookTags();
    private boolean isContinue = false;           //Helps with parsing non-ASCII characters

    public SAXHandler(Data Books){

        books = Books;

    }

    public Data getBooks()
    {
        return books;
    }
    @Override
    public void startDocument() throws SAXException
    {
    }
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
    {
        if("entry".equals(qName)){
            isEntryTag = true;
            books.getInfo().add(new DataElement());
        }

        for(int i = 0; i < tags.getTags().length; ++i){
            tags.getTags()[i].setStatus(tags.getTags()[i].getName().equals(qName));
        }
    }
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException
    {
        if (isEntryTag){
            for(int i = 0; i < tags.getTags().length; ++i){
                if(tags.getTags()[i].getStatus()) {
        	        books.getInfo().lastElement().getFields()[i] = new String(ch, start, length);
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
            isEntryTag = false;
        }
        for(int i = 0; i < tags.getTags().length; ++i){
            tags.getTags()[i].setStatus(false);
        }
    }
    @Override
    public void endDocument() throws SAXException
    {
    }

}
