package org.ebooksearchtool.client.logic.parsing;

import org.ebooksearchtool.client.model.QueryAnswer;
import org.ebooksearchtool.client.model.books.Data;
import org.ebooksearchtool.client.model.books.Book;
import org.ebooksearchtool.client.model.books.Author;
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

    private QueryAnswer myAnswer;
    private boolean myIsEntryTag = false;
    private BookTags myBookTags = new BookTags();
    private AuthorTags myAuthorTags = new AuthorTags();
    private boolean myIsContinue = false;           //Helps with parsing non-ASCII characters
    private Author myCurAuthor;
    private boolean myIsTotalTag;

    public SAXHandler(QueryAnswer answer){

        myAnswer = answer;

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
            myAnswer.getData().addBook(new Book());
        }
        
        if("opensearch:totalResults".equals(qName)){
        	myIsTotalTag = true;
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
                		myAnswer.getData().getBookLinks(myAnswer.getData().getBooks().size()-1).put("pdf", attributes.getValue("href"));
                	}else if(myIsEntryTag && attributes.getValue(j).equals("application/epub+zip")){
                        myAnswer.getData().getBookLinks(myAnswer.getData().getBooks().size()-1).put("epub", attributes.getValue("href"));
                    }else if(myIsEntryTag && attributes.getValue(j).equals("image/png")){
                        myAnswer.getData().setBookImage(myAnswer.getData().getBooks().size()-1, attributes.getValue("href"));
                    }else if(myIsEntryTag && attributes.getLocalName(j).equals("term")){
                        myAnswer.getData().setBookGenre(myAnswer.getData().getBooks().size()-1, attributes.getValue(j));
                    }else if(attributes.getValue(j).equals("Next Page")){
                    	myAnswer.setNextPage(new String(attributes.getValue("href")));
                    	
                    }
                }
                    
                
            }

        }

    }
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException
    {
    	
    	if (myIsTotalTag){
    		myAnswer.setTotalBooksNumber(Integer.parseInt((new String(ch, start, length)).trim()));
    	}
    	
        if (myIsEntryTag){
            for(int i = 0; i < myBookTags.getTags().length; ++i){
                if(myBookTags.getTags()[i].getStatus()) {
                	
                    if(myIsContinue){
                        if(myBookTags.getTags()[i].getName().equals("title")){
                        	myAnswer.getData().setBookTitle(myAnswer.getData().getBooks().size()-1, myAnswer.getData().getBooks().get(myAnswer.getData().getBooks().size()-1).getTitle() + new String(ch, start, length));
                        }else if(myBookTags.getTags()[i].getName().equals("dcterms:language")){
                        	myAnswer.getData().setBookLanguage(myAnswer.getData().getBooks().size()-1, myAnswer.getData().getBooks().get(myAnswer.getData().getBooks().size()-1).getLanguage() + new String(ch, start, length));
                        }else if(myBookTags.getTags()[i].getName().equals("dcterms:issued")){
                        	myAnswer.getData().setBookDate(myAnswer.getData().getBooks().size()-1, myAnswer.getData().getBooks().get(myAnswer.getData().getBooks().size()-1).getDate() + new String(ch, start, length));
                        }else if(myBookTags.getTags()[i].getName().equals("summary")){
                        	myAnswer.getData().setBookSummary(myAnswer.getData().getBooks().size()-1, myAnswer.getData().getBooks().get(myAnswer.getData().getBooks().size()-1).getSummary() + new String(ch, start, length));
                        }
                    }else{
                        if(myBookTags.getTags()[i].getName().equals("title")){
                        	myAnswer.getData().setBookTitle(myAnswer.getData().getBooks().size()-1, new String(ch, start, length));
                        	myIsContinue = true;
                        }else if(myBookTags.getTags()[i].getName().equals("dcterms:language")){
                        	myAnswer.getData().setBookLanguage(myAnswer.getData().getBooks().size()-1, new String(ch, start, length));
                        	myIsContinue = true;
                        }else if(myBookTags.getTags()[i].getName().equals("dcterms:issued")){
                        	myAnswer.getData().setBookDate(myAnswer.getData().getBooks().size()-1, new String(ch, start, length));
                        	myIsContinue = true;
                        }else if(myBookTags.getTags()[i].getName().equals("summary")){
                        	myAnswer.getData().setBookSummary(myAnswer.getData().getBooks().size()-1, new String(ch, start, length));
                        	myIsContinue = true;
                        }
                        else if(myBookTags.getTags()[i].getName().equals("id")){
                        	myAnswer.getData().setBookID(myAnswer.getData().getBooks().size()-1, new String(ch, start, length));
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
        
        if("opensearch:totalResults".equals(qName)){
        	myIsTotalTag = false;
        }
        
        if("author".equals(qName) && myIsEntryTag){
            boolean authorExists = false;
            for(int j = 0; j < myAnswer.getData().getAuthors().size(); ++j){
                if(myCurAuthor.getID().equals(myAnswer.getData().getAuthors().get(j).getID())){
                    authorExists = true;
                    myAnswer.getData().setBookAuthor(myAnswer.getData().getBooks().size()-1, myAnswer.getData().getAuthors().get(j));
                }
            }
            if(!authorExists){
                myAnswer.getData().setBookAuthor(myAnswer.getData().getBooks().size()-1, myCurAuthor);
                myAnswer.getData().addAuthor(myCurAuthor);
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
