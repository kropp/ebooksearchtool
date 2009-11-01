package org.ebooksearchtool.client.logic.parsing;

import org.ebooksearchtool.client.model.Data;
import org.ebooksearchtool.client.model.Book;
import org.ebooksearchtool.client.model.Author;
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

    private Data myData;
    private boolean myIsEntryTag = false;
    private BookTags myBookTags = new BookTags();
    private AuthorTags myAuthorTags = new AuthorTags();
    private boolean myIsContinue = false;           //Helps with parsing non-ASCII characters
    private Author myCurAuthor;

    public SAXHandler(Data Books){

        myData = Books;

    }

    public Data getBooks()
    {
        return myData;
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
            myData.addBook(new Book());
        }

        if("author".equals(qName) && myIsEntryTag){
            myCurAuthor = new Author();
        }

        for(int i = 0; i < myAuthorTags.getTags().length; ++i){
            if(myAuthorTags.getTags()[i].getName().equals(qName) && myIsEntryTag){
                myAuthorTags.getTags()[i].setStatus(true);
            }
        }

        for(int i = 0; i < myBookTags.getTags().length; ++i){
            myBookTags.getTags()[i].setStatus(myBookTags.getTags()[i].getName().equals(qName));
            if (attributes != null) {
                int len = attributes.getLength();
                for (int j = 0; j < len; ++j)
                {
                	if(myIsEntryTag && attributes.getValue(j).equals("application/pdf")){
                		myData.setBookPdfLink(myData.getBooks().size()-1, attributes.getValue(j+2));
                	}else if(myIsEntryTag && attributes.getValue(j).equals("application/epub+zip")){
                        myData.setBookEpubLink(myData.getBooks().size()-1, attributes.getValue(j+2));
                    }else if(myIsEntryTag && attributes.getValue(j).equals("image/png")){
                        myData.setBookImage(myData.getBooks().size()-1, attributes.getValue(j+2));
                    }else if(myIsEntryTag && attributes.getLocalName(j).equals("term")){
                        myData.setBookGenre(myData.getBooks().size()-1, attributes.getValue(j));
                    }
                }
                    
                
            }

        }

    }
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException
    {
        if (myIsEntryTag){
            for(int i = 0; i < myBookTags.getTags().length; ++i){
                if(myBookTags.getTags()[i].getStatus()) {
                	
                    if(myIsContinue){
                        if(myBookTags.getTags()[i].getName().equals("title")){
                        	myData.setBookTitle(myData.getBooks().size()-1, myData.getBooks().get(myData.getBooks().size()-1).getTitle() + new String(ch, start, length));
                        }else if(myBookTags.getTags()[i].getName().equals("dcterms:language")){
                        	myData.setBookLanguage(myData.getBooks().size()-1, myData.getBooks().get(myData.getBooks().size()-1).getLanguage() + new String(ch, start, length));
                        }else if(myBookTags.getTags()[i].getName().equals("dcterms:issued")){
                        	myData.setBookDate(myData.getBooks().size()-1, myData.getBooks().get(myData.getBooks().size()-1).getDate() + new String(ch, start, length));
                        }else if(myBookTags.getTags()[i].getName().equals("summary")){
                        	myData.setBookSummary(myData.getBooks().size()-1, myData.getBooks().get(myData.getBooks().size()-1).getSummary() + new String(ch, start, length));
                        }
                    }else{
                        if(myBookTags.getTags()[i].getName().equals("title")){
                        	myData.setBookTitle(myData.getBooks().size()-1, new String(ch, start, length));
                        	myIsContinue = true;
                        }else if(myBookTags.getTags()[i].getName().equals("dcterms:language")){
                        	myData.setBookLanguage(myData.getBooks().size()-1, new String(ch, start, length));
                        	myIsContinue = true;
                        }else if(myBookTags.getTags()[i].getName().equals("dcterms:issued")){
                        	myData.setBookDate(myData.getBooks().size()-1, new String(ch, start, length));
                        	myIsContinue = true;
                        }else if(myBookTags.getTags()[i].getName().equals("summary")){
                        	myData.setBookSummary(myData.getBooks().size()-1, new String(ch, start, length));
                        	myIsContinue = true;
                        }
                        else if(myBookTags.getTags()[i].getName().equals("id")){
                        	myData.setBookID(myData.getBooks().size()-1, new String(ch, start, length));
                        }
                    }
                }
            }
            for(int i = 0; i < myAuthorTags.getTags().length; ++i){
                if(myAuthorTags.getTags()[i].getStatus()) {
                    if(myAuthorTags.getTags()[i].getName().equals("name")){
                                    myCurAuthor.setName(new String(ch, start, length));
                                }else if(myAuthorTags.getTags()[i].getName().equals("uri")){
                                    myCurAuthor.setID(new String(ch, start, length));
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
        if("author".equals(qName) && myIsEntryTag){
            boolean authorExists = false;
            for(int j = 0; j < myData.getAuthors().size(); ++j){
                if(myCurAuthor.getID().equals(myData.getAuthors().get(j).getID())){
                    authorExists = true;
                    myData.setBookAuthor(myData.getBooks().size()-1, myData.getAuthors().get(j));
                }
            }
            if(!authorExists){
                myData.setBookAuthor(myData.getBooks().size()-1, myCurAuthor);
                myData.addAuthor(myCurAuthor);
            }
        }
        for(int i = 0; i < myBookTags.getTags().length; ++i){
            myBookTags.getTags()[i].setStatus(false);
            myIsContinue = false;
        }
        for(int i = 0; i < myAuthorTags.getTags().length; ++i){
            myAuthorTags.getTags()[i].setStatus(false);    
        }
    }
    @Override
    public void endDocument() throws SAXException
    {
    }

}
