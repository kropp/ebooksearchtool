package org.ebooksearchtool.client.logic.parsing;

import org.ebooksearchtool.client.model.Data;
import org.ebooksearchtool.client.model.Book;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

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
            myBooks.addElement(new Book());
        }

        for(int i = 0; i < myTags.getTags().length; ++i){
            myTags.getTags()[i].setStatus(myTags.getTags()[i].getName().equals(qName));
            if (attributes != null) {
            	boolean isPdf = false;
                int len = attributes.getLength();
                for (int j = 0; j < len; ++j)
                {
                	System.out.println(attributes.getValue(j));
                	if(attributes.getValue(j).equals("application/pdf")){
                		isPdf = true;
                	}
                	if(myIsEntryTag && isPdf){
                		myBooks.setBookLink(myBooks.getBooks().size()-1, attributes.getValue(j));
                	}
                }
                    
                
            }

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
                        if(myTags.getTags()[i].getName() == "title"){
                        	myBooks.setBookTitle(myBooks.getBooks().size()-1, myBooks.getBooks().get(myBooks.getBooks().size()-1).getTitle() + new String(ch, start, length));
                        }else if(myTags.getTags()[i].getName() == "name"){
                        	myBooks.setBookAuthor(myBooks.getBooks().size()-1, myBooks.getBooks().get(myBooks.getBooks().size()-1).getAuthor() + new String(ch, start, length));
                        }else if(myTags.getTags()[i].getName() == "dcterms:language"){
                        	myBooks.setBookLanguage(myBooks.getBooks().size()-1, myBooks.getBooks().get(myBooks.getBooks().size()-1).getLanguage() + new String(ch, start, length));
                        }else if(myTags.getTags()[i].getName() == "dcterms:issued"){
                        	myBooks.setBookDate(myBooks.getBooks().size()-1, myBooks.getBooks().get(myBooks.getBooks().size()-1).getDate() + new String(ch, start, length));
                        }else if(myTags.getTags()[i].getName() == "summary"){
                        	myBooks.setBookSummary(myBooks.getBooks().size()-1, myBooks.getBooks().get(myBooks.getBooks().size()-1).getSummary() + new String(ch, start, length));
                        }else if(myTags.getTags()[i].getName() == "id"){
                        	myBooks.setBookLink(myBooks.getBooks().size()-1, myBooks.getBooks().get(myBooks.getBooks().size()-1).getLink() + new String(ch, start, length));
                        }                    
                    }else{
                        if(myTags.getTags()[i].getName() == "title"){
                        	myBooks.setBookTitle(myBooks.getBooks().size()-1, new String(ch, start, length));
                        	myIsContinue = true;
                        }else if(myTags.getTags()[i].getName() == "name"){
                        	myBooks.setBookAuthor(myBooks.getBooks().size()-1, new String(ch, start, length));
                        	myIsContinue = true;
                        }else if(myTags.getTags()[i].getName() == "dcterms:language"){
                        	myBooks.setBookLanguage(myBooks.getBooks().size()-1, new String(ch, start, length));
                        	myIsContinue = true;
                        }else if(myTags.getTags()[i].getName() == "dcterms:issued"){
                        	myBooks.setBookDate(myBooks.getBooks().size()-1, new String(ch, start, length));
                        	myIsContinue = true;
                        }else if(myTags.getTags()[i].getName() == "summary"){
                        	myBooks.setBookSummary(myBooks.getBooks().size()-1, new String(ch, start, length));
                        	myIsContinue = true;
                        }
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
    	for (int i = 0; i < myBooks.getBooks().size(); ++i){
    		System.out.println(myBooks.getBooks().get(i).getTitle() + "   " + myBooks.getBooks().get(i).getLink());
    	}
    }

}
