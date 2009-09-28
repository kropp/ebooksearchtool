package Logic.Parser;

import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.SAXException;

import org.xml.sax.Attributes;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 25.09.2009
 * Time: 13:25:52
 * To change this template use File | Settings | File Templates.
 */
public class SAXHandler extends DefaultHandler{

    private ArrayList<String> books;
    private boolean isUserTag = false;
    private boolean isContinue = false;
    public String[] getBooks()
    {
        return books.toArray(new String[books.size()]);
    }
    @Override
    public void startDocument() throws SAXException
    {
        books = new ArrayList<String>();
    }
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
    {
        isUserTag = "title".equals(qName);
    }
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException
    {
        if(isUserTag) {
        	if(isContinue) {
        		books.set(books.size()-1, books.get(books.size()-1)+(new String(ch, start, length)));
        	}else{
        		books.add(new String(ch, start, length));
            	isContinue = isUserTag;
        	}
        }
    }
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException
    {
        isUserTag = false;
        isContinue = false;
    }
    @Override
    public void endDocument() throws SAXException
    {
    }

}
