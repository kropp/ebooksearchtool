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
        if(!localName.equals(qName)){
            if("http://purl.org/dc/terms/".equals(uri) || "http://a9.com/-/spec/opensearch/1.1/".equals(uri)){
                qName = localName;
            }
        }

        if("entry".equals(qName)){
            myIsEntryTag = true;
            myAnswer.getData().addBook(new Book());
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

            if("content".equals(myBookTags.getTags()[i].getName())){
                if(myBookTags.getTags()[i].getStatus()){
                    String tagString = qName;
                    for(int j = 0; j < attributes.getLength(); ++j){
                        tagString = tagString + " " + attributes.getQName(j) + "=\"" + attributes.getValue(j) + "\"";
                    }
                    myAnswer.getData().setBookContent(myAnswer.getData().getBooks().size()-1, myAnswer.getData().getBooks().get(myAnswer.getData().getBooks().size()-1).getContent() + "<" + tagString + ">");
                }
            }

            if(myBookTags.getTags()[i].getName().equals(qName)){
                myBookTags.getTags()[i].setStatus(true);
            }
            if (attributes != null) {
                if(myIsEntryTag){
                    if("application/pdf".equals(attributes.getValue("type"))){
                        myAnswer.getData().getBookLinks(myAnswer.getData().getBooks().size()-1).put("pdf", attributes.getValue("href"));
                    }else if("application/epub+zip".equals(attributes.getValue("type"))){
                        myAnswer.getData().getBookLinks(myAnswer.getData().getBooks().size()-1).put("epub", attributes.getValue("href"));
                    }else if("http://opds-spec.org/thumbnail".equals(attributes.getValue("rel"))){
                        myAnswer.getData().setBookImage(myAnswer.getData().getBooks().size()-1, attributes.getValue("href"));
                    }else if("x-stanza-cover-image-thumbnail".equals(attributes.getValue("rel"))){
                        myAnswer.getData().setBookImage(myAnswer.getData().getBooks().size()-1, attributes.getValue("href"));
                    }else if(attributes.getValue("term") != null){
                        myAnswer.getData().setBookGenre(myAnswer.getData().getBooks().size()-1, attributes.getValue("term"));
                    }else if("start".equals(attributes.getValue("rel"))){
                        myAnswer.setCatMainPage(attributes.getValue("href"));
                    }
                }else if("next".equals(attributes.getValue("rel")) && "application/atom+xml".equals(attributes.getValue("type"))){
                    myAnswer.setNextPage(attributes.getValue("href"));

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
                        }else if(myBookTags.getTags()[i].getName().equals("language")){
                        	myAnswer.getData().setBookLanguage(myAnswer.getData().getBooks().size()-1, myAnswer.getData().getBooks().get(myAnswer.getData().getBooks().size()-1).getLanguage() + new String(ch, start, length));
                        }else if(myBookTags.getTags()[i].getName().equals("publisher")){
                        	myAnswer.getData().setBookPublisher(myAnswer.getData().getBooks().size()-1, myAnswer.getData().getBooks().get(myAnswer.getData().getBooks().size()-1).getPublisher() + new String(ch, start, length));
                        }else if(myBookTags.getTags()[i].getName().equals("rights")){
                        	myAnswer.getData().setBookRights(myAnswer.getData().getBooks().size()-1, myAnswer.getData().getBooks().get(myAnswer.getData().getBooks().size()-1).getRights() + new String(ch, start, length));
                        }else if(myBookTags.getTags()[i].getName().equals("issued")){
                        	myAnswer.getData().setBookDate(myAnswer.getData().getBooks().size()-1, myAnswer.getData().getBooks().get(myAnswer.getData().getBooks().size()-1).getDate() + new String(ch, start, length));
                        }else if(myBookTags.getTags()[i].getName().equals("updated")){
                        	myAnswer.getData().setBookUpdateTime(myAnswer.getData().getBooks().size()-1, myAnswer.getData().getBooks().get(myAnswer.getData().getBooks().size()-1).getUpdateTime() + new String(ch, start, length));
                        }else if(myBookTags.getTags()[i].getName().equals("summary")){
                        	myAnswer.getData().setBookSummary(myAnswer.getData().getBooks().size()-1, myAnswer.getData().getBooks().get(myAnswer.getData().getBooks().size()-1).getSummary() + new String(ch, start, length));
                        }else if(myBookTags.getTags()[i].getName().equals("content")){
                        	myAnswer.getData().setBookContent(myAnswer.getData().getBooks().size()-1, myAnswer.getData().getBooks().get(myAnswer.getData().getBooks().size()-1).getContent() + new String(ch, start, length));
                        }
                    }else{
                        if(myBookTags.getTags()[i].getName().equals("title")){
                            myAnswer.getData().setBookTitle(myAnswer.getData().getBooks().size()-1, new String(ch, start, length));
                        	myIsContinue = true;
                        }else if(myBookTags.getTags()[i].getName().equals("language")){
                        	myAnswer.getData().setBookLanguage(myAnswer.getData().getBooks().size()-1, new String(ch, start, length));
                        	myIsContinue = true;
                        }else if(myBookTags.getTags()[i].getName().equals("publisher")){
                        	myAnswer.getData().setBookPublisher(myAnswer.getData().getBooks().size()-1, new String(ch, start, length));
                        	myIsContinue = true;
                        }else if(myBookTags.getTags()[i].getName().equals("rights")){
                        	myAnswer.getData().setBookRights(myAnswer.getData().getBooks().size()-1, new String(ch, start, length));
                        	myIsContinue = true;
                        }else if(myBookTags.getTags()[i].getName().equals("issued")){
                        	myAnswer.getData().setBookDate(myAnswer.getData().getBooks().size()-1, new String(ch, start, length));
                        	myIsContinue = true;
                        }else if(myBookTags.getTags()[i].getName().equals("updated")){
                        	myAnswer.getData().setBookUpdateTime(myAnswer.getData().getBooks().size()-1, new String(ch, start, length));
                        	myIsContinue = true;
                        }else if(myBookTags.getTags()[i].getName().equals("summary")){
                        	myAnswer.getData().setBookSummary(myAnswer.getData().getBooks().size()-1, new String(ch, start, length));
                        	myIsContinue = true;
                        }else if(myBookTags.getTags()[i].getName().equals("content")){
                        	myAnswer.getData().setBookContent(myAnswer.getData().getBooks().size()-1, new String(ch, start, length));
                        	myIsContinue = true;
                        }else if(myBookTags.getTags()[i].getName().equals("id")){
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
        if(!localName.equals(qName)){
            if("http://purl.org/dc/terms/".equals(uri) || "http://a9.com/-/spec/opensearch/1.1/".equals(uri)){
                qName = localName;
            }
        }

        if(qName.equals("content") && myAnswer.getData().getBooks().size() != 0){
            //System.out.println(myAnswer.getData().getBooks().get(myAnswer.getData().getBooks().size()-1).getContent());
        }

        if("entry".equals(qName)){
            myIsEntryTag = false;
        }
        
        if("totalResults".equals(qName)){
        	myIsTotalTag = false;
        }

        if("title".equals(qName) && myAnswer.getData().getBooks().size() != 0){
            StringBuffer result = new StringBuffer(myAnswer.getData().getBooks().get(myAnswer.getData().getBooks().size() - 1).getTitle());
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
            myAnswer.getData().getBooks().get(myAnswer.getData().getBooks().size() - 1).setTitle(result.toString());
        }
        
        if("author".equals(qName) && myIsEntryTag){
            boolean authorExists = false;
            for(int j = 0; j < myAnswer.getData().getAuthors().size(); ++j){
                if((myCurAuthor.getID() != null) && myCurAuthor.getID().equals(myAnswer.getData().getAuthors().get(j).getID())){
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
            if(qName.equals(myBookTags.getTags()[i].getName())){
                myBookTags.getTags()[i].setStatus(false);
                myIsContinue = false;
            }

            if("content".equals(myBookTags.getTags()[i].getName())){
                if(myBookTags.getTags()[i].getStatus()){
                    myAnswer.getData().setBookContent(myAnswer.getData().getBooks().size()-1, myAnswer.getData().getBooks().get(myAnswer.getData().getBooks().size()-1).getContent() + "<" + qName + "/>");
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
