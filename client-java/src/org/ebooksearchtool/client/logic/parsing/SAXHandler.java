package org.ebooksearchtool.client.logic.parsing;

import org.ebooksearchtool.client.model.QueryAnswer;
import org.ebooksearchtool.client.model.books.Book;
import org.ebooksearchtool.client.model.books.Author;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.swing.*;
import java.io.IOException;

/*
 * Date: 25.09.2009
 * Time: 13:25:52
 */
public class SAXHandler extends DefaultHandler{

    private QueryAnswer myAnswer;
    private boolean myIsEntryTag = false;
    private BookTags myBookTags = new BookTags();
    private AuthorTags myAuthorTags = new AuthorTags();
    private boolean myIsContinue = false;           //Helps with parsing non-ASCII characters
    private Author myCurAuthor;
    private boolean myIsTotalTag;
    String mySource;
    boolean myIsSource;

    Book myCurBook;

    public SAXHandler(QueryAnswer answer){

        myAnswer = answer;

    }

    @Override
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        return new org.xml.sax.InputSource(new java.io.StringReader(""));
    }

    @Override
    public void startDocument() throws SAXException
    {
        myAnswer.setNextPage("");
    }
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
    {
        if(!localName.equals(qName)){
            if("http://purl.org/dc/terms/".equals(uri) || "http://a9.com/-/spec/opensearch/1.1/".equals(uri)){
                qName = localName;
            }
        }

        if("entry".equals(qName)){
            myIsEntryTag = true;
            myCurBook = new Book();
            myCurBook.setSource(mySource);
        }

        if("totalResults".equals(qName)){
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

            if("content".equals(myBookTags.getTags()[i].getName()) && myIsEntryTag){
                if(myBookTags.getTags()[i].getStatus()){
                    String tagString = qName;
                    for(int j = 0; j < attributes.getLength(); ++j){
                        tagString = tagString + " " + attributes.getQName(j) + "=\"" + attributes.getValue(j) + "\"";
                    }
                    myCurBook.setContent(myCurBook.getContent() + "<" + tagString + ">");
                }
            }

            if(myBookTags.getTags()[i].getName().equals(qName)){
                myBookTags.getTags()[i].setStatus(true);
            }

        }

        if (attributes != null) {
            if (myIsEntryTag) {
                if ("application/pdf".equals(attributes.getValue("type"))) {
                    myCurBook.getLinks().put("pdf", attributes.getValue("href"));
                } else if ("application/epub+zip".equals(attributes.getValue("type"))) {
                    myCurBook.getLinks().put("epub", attributes.getValue("href"));
                } else if ("http://opds-spec.org/thumbnail".equals(attributes.getValue("rel"))) {
                    myCurBook.setImage(attributes.getValue("href"));
                } else if ("x-stanza-cover-image-thumbnail".equals(attributes.getValue("rel"))) {
                    myCurBook.setImage(attributes.getValue("href"));
                } else if ("category".equals(qName) && attributes.getValue("term") != null) {
                    myCurBook.setGenre(attributes.getValue("term"));
                } else if ("start".equals(attributes.getValue("rel"))) {
                    myAnswer.setCatMainPage(attributes.getValue("href"));
                }
            } else if ("next".equals(attributes.getValue("rel")) && "application/atom+xml".equals(attributes.getValue("type"))) {
                myAnswer.setNextPage(attributes.getValue("href"));

            }

        }

        if(!myIsEntryTag && myBookTags.getTags()[2].getStatus() && "name".equals(qName)){
            myIsSource = true;
        }

    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {

    	if (myIsTotalTag){
    		myAnswer.setTotalBooksNumber(Integer.parseInt((new String(ch, start, length)).trim()));
    	}

        if(myIsSource){
            mySource = new String(ch, start, length);
        }

        if (myIsEntryTag){
            for(int i = 0; i < myBookTags.getTags().length; ++i){
                if(myBookTags.getTags()[i].getStatus()) {
                	
                    if(myIsContinue){
                        if(myBookTags.getTags()[i].getName().equals("title")){
                            myCurBook.setTitle(myCurBook.getTitle() + new String(ch, start, length));
                        }else if(myBookTags.getTags()[i].getName().equals("subtitle")){
                            myCurBook.setSubtitle(myCurBook.getSubtitle() + new String(ch, start, length));
                        }else if(myBookTags.getTags()[i].getName().equals("language")){
                        	myCurBook.setLanguage(myCurBook.getLanguage() + new String(ch, start, length));
                        }else if(myBookTags.getTags()[i].getName().equals("publisher")){
                        	myCurBook.setPublisher(myCurBook.getPublisher() + new String(ch, start, length));
                        }else if(myBookTags.getTags()[i].getName().equals("rights")){
                        	myCurBook.setRights(myCurBook.getRights() + new String(ch, start, length));
                        }else if(myBookTags.getTags()[i].getName().equals("issued")){
                        	myCurBook.setDate(myCurBook.getDate() + new String(ch, start, length));
                        }else if(myBookTags.getTags()[i].getName().equals("updated")){
                        	myCurBook.setUpdateTime(myCurBook.getUpdateTime() + new String(ch, start, length));
                        }else if(myBookTags.getTags()[i].getName().equals("summary")){
                        	myCurBook.setSummary(myCurBook.getSummary() + new String(ch, start, length));
                        }else if(myBookTags.getTags()[i].getName().equals("content")){
                        	myCurBook.setContent(myCurBook.getContent() + new String(ch, start, length));
                        }
                    }else{
                        if(myBookTags.getTags()[i].getName().equals("title")){
                            myCurBook.setTitle(new String(ch, start, length));
                        	myIsContinue = true;
                        }else if(myBookTags.getTags()[i].getName().equals("subtitle")){
                            myCurBook.setSubtitle(new String(ch, start, length));
                        	myIsContinue = true;
                        }else if(myBookTags.getTags()[i].getName().equals("sourceServer")){
                            myCurBook.setSource(new String(ch, start, length));
                        	myIsContinue = true;
                        }else if(myBookTags.getTags()[i].getName().equals("language")){
                        	myCurBook.setLanguage(new String(ch, start, length));
                        	myIsContinue = true;
                        }else if(myBookTags.getTags()[i].getName().equals("publisher")){
                        	myCurBook.setPublisher(new String(ch, start, length));
                        	myIsContinue = true;
                        }else if(myBookTags.getTags()[i].getName().equals("rights")){
                        	myCurBook.setRights(new String(ch, start, length));
                        	myIsContinue = true;
                        }else if(myBookTags.getTags()[i].getName().equals("issued")){
                        	myCurBook.setDate(new String(ch, start, length));
                        	myIsContinue = true;
                        }else if(myBookTags.getTags()[i].getName().equals("updated")){
                        	myCurBook.setUpdateTime(new String(ch, start, length));
                        	myIsContinue = true;
                        }else if(myBookTags.getTags()[i].getName().equals("summary")){
                        	myCurBook.setSummary(new String(ch, start, length));
                        	myIsContinue = true;
                        }else if(myBookTags.getTags()[i].getName().equals("content")){
                        	myCurBook.setContent(new String(ch, start, length));
                        	myIsContinue = true;
                        }else if(myBookTags.getTags()[i].getName().equals("id")){
                        	myCurBook.setID(new String(ch, start, length));
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
        if(!localName.equals(qName)){
            if("http://purl.org/dc/terms/".equals(uri) || "http://a9.com/-/spec/opensearch/1.1/".equals(uri)){
                qName = localName;
            }
        }

        if(!myIsEntryTag && myBookTags.getTags()[2].getStatus() && "name".equals(qName)){
            myIsSource = false;
        }

        if(qName.equals("content") && myAnswer.getData().getBooks().size() != 0){
            //System.out.println(myAnswer.getData().getBooks().get(myAnswer.getData().getBooks().size()-1).getContent());
        }

        if("entry".equals(qName)){
            myIsEntryTag = false;

            synchronized (myAnswer.getData()) {
                myAnswer.getData().addBook(myCurBook);
                myAnswer.getData().notifyObservers(myCurBook);
            }

        }
        
        if("totalResults".equals(qName)){
        	myIsTotalTag = false;
        }

        if(myIsEntryTag && "title".equals(qName) && myAnswer.getData().getBooks().size() != 0){
            StringBuffer result = new StringBuffer(myCurBook.getTitle());
            while(result.indexOf("\n") != -1){
                result.deleteCharAt(result.indexOf("\n"));
            }
            while(result.lastIndexOf(" ") == (result.length() - 1)){
                result.deleteCharAt(result.lastIndexOf(" "));
            }
            while(result.indexOf(" ") == 0){
                result.deleteCharAt(result.indexOf(" "));
            }
            while(result.indexOf("\t") == 0){
                result.deleteCharAt(result.indexOf("\t"));
            }
            myCurBook.setTitle(result.toString());
        }
        
        if("author".equals(qName) && myIsEntryTag){
            boolean authorExists = false;

            synchronized (myAnswer.getData()) {
                for (int j = 0; j < myAnswer.getData().getAuthors().size(); ++j) {
                    if ((myCurAuthor.getID() != null) && myCurAuthor.getID().equals(myAnswer.getData().getAuthors().get(j).getID())) {
                        authorExists = true;
                        myCurBook.setAuthor(myAnswer.getData().getAuthors().get(j));
                    }
                }
                if (!authorExists) {
                    myCurBook.setAuthor(myCurAuthor);
                    myAnswer.getData().addAuthor(myCurAuthor);
                }
            }

        }
        for(int i = 0; i < myBookTags.getTags().length; ++i){
            if(qName.equals(myBookTags.getTags()[i].getName())){
                myBookTags.getTags()[i].setStatus(false);
                myIsContinue = false;
            }

            if("content".equals(myBookTags.getTags()[i].getName())){
                if(myBookTags.getTags()[i].getStatus()){
                    myCurBook.setContent(myCurBook.getContent() + "<" + qName + "/>");
                }
            }

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
