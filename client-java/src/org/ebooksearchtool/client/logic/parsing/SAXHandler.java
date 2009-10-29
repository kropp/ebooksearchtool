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

        for(int i = 0; i < myAuthorTags.getTags().length; ++i){
            myAuthorTags.getTags()[i].setStatus(myAuthorTags.getTags()[i].getName().equals(qName));    
        }

        for(int i = 0; i < myBookTags.getTags().length; ++i){
            myBookTags.getTags()[i].setStatus(myBookTags.getTags()[i].getName().equals(qName));
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
                		myData.setBookLink(myData.getBooks().size()-1, attributes.getValue(j));
                	}
                }
                    
                
            }

        }

/*        for (int i = 0; i < 2; ++i){
            System.out.println(myData.getClass().getDeclaredFields()[i]);
        }
        for (int i = 0; i < 8; ++i){
            System.out.println(myData.getClass().getMethods()[i]);
        }
  */
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
                        }else if(myBookTags.getTags()[i].getName().equals("author")){
                            Author curAuthor = new Author();
                            curAuthor.addBook(myData.getBooks().get(myData.getBooks().size()-1));

                            if(myAuthorTags.getTags()[i].getName().equals("name")){
                                curAuthor.setName(new String(ch, start, length));
                            }else if(myAuthorTags.getTags()[i].getName().equals("uri")){
                                curAuthor.setID(new String(ch, start, length));
                            }
                            boolean authorExists = false;
                            for(int j = 0; j < myData.getAuthors().size(); ++j){
                                if(curAuthor.getID().equals(myData.getAuthors().get(j).getID())){
                                    authorExists = true;
                                    myData.setBookAuthor(myData.getBooks().size()-1, myData.getAuthors().get(j));

                                }
                            }
                            if(!authorExists){
                                myData.setBookAuthor(myData.getBooks().size()-1, curAuthor);
                                myData.addAuthor(curAuthor);
                            }

                        }else if(myBookTags.getTags()[i].getName().equals("dcterms:language")){
                        	myData.setBookLanguage(myData.getBooks().size()-1, myData.getBooks().get(myData.getBooks().size()-1).getLanguage() + new String(ch, start, length));
                        }else if(myBookTags.getTags()[i].getName().equals("dcterms:issued")){
                        	myData.setBookDate(myData.getBooks().size()-1, myData.getBooks().get(myData.getBooks().size()-1).getDate() + new String(ch, start, length));
                        }else if(myBookTags.getTags()[i].getName().equals("summary")){
                        	myData.setBookSummary(myData.getBooks().size()-1, myData.getBooks().get(myData.getBooks().size()-1).getSummary() + new String(ch, start, length));
                        }else if(myBookTags.getTags()[i].getName().equals("id")){
                        	myData.setBookLink(myData.getBooks().size()-1, myData.getBooks().get(myData.getBooks().size()-1).getLink() + new String(ch, start, length));
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
        for(int i = 0; i < myBookTags.getTags().length; ++i){
            myBookTags.getTags()[i].setStatus(false);
            myIsContinue = false;
        }
    }
    @Override
    public void endDocument() throws SAXException
    {
    	for (int i = 0; i < myData.getBooks().size(); ++i){
    		System.out.println(myData.getBooks().get(i).getTitle() + "   " + myData.getBooks().get(i).getLink());
    	}
    }

}
