package org.ebooksearchtool.analyzer.model;

/**
 * @author Алексей
 */

import java.util.ArrayList;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class ServerSearchRequestHandler extends DefaultHandler{

    private boolean myInfoFlag = false;
    private boolean myAuthorFlag = false;
    private boolean myTitleFlag = false;
    private boolean myRelFlag = false;
    private boolean myCreditFlag = false;
    private boolean myNameFlag = false;
    private ArrayList<Author> authors;
    private ArrayList<Title> titles;
    private String myID;

    public ServerSearchRequestHandler(){
        super();
        authors = new ArrayList<Author>();
        titles = new ArrayList<Title>();
        myID = "";
    }

    @Override
    public void characters (char ch[], int start, int length)
            throws SAXException{
        if(myInfoFlag){
            if(myAuthorFlag){
                if(myNameFlag){
                authors.add(new Author(new String(ch, start, length).trim()));
                if(!myID.equals("")){
                    authors.get(authors.size()).setID(myID);
                }
                }
                if(myRelFlag){
                    authors.get(authors.size()).setRelIndex(Integer.parseInt(
                            new String(ch, start, length).trim()));
                }
                if(myCreditFlag){
                    authors.get(authors.size()).setTrIndex(Integer.parseInt(
                            new String(ch, start, length).trim()));
                }
            }
            if(myTitleFlag == true){
                if(myNameFlag){
                titles.add(new Title(new String(ch, start, length).trim()));
                if(!myID.equals("")){
                    titles.get(titles.size()).setID(myID);
                }
                }
                if(myRelFlag){
                    titles.get(titles.size()).setRelIndex(Integer.parseInt(
                            new String(ch, start, length).trim()));
                }
                if(myCreditFlag){
                    titles.get(titles.size()).setTrIndex(Integer.parseInt(
                            new String(ch, start, length).trim()));
                }
            }
        }
    }

    @Override
    public void endElement (String uri, String localName, String qName)
	throws SAXException
    {
        if(qName.equals("info")){
            myInfoFlag = false;
        }
        if(qName.equals("Author")){
            myAuthorFlag = false;
        }
        if(qName.equals("Rel")){
            myRelFlag = false;
        }
        if(qName.equals("Title")){
            myTitleFlag = false;
        }
        if(qName.equals("Credit")){
            myCreditFlag = false;
        }
    }
    
    @Override
    public void startElement (String uri, String localName,
			      String qName, Attributes attributes)
	throws SAXException
    {
        if(qName.equals("info")){
            myInfoFlag = true;
        }
        if(qName.equals("Author")){
            myAuthorFlag = true;
            myID = attributes.getValue("ID");
        }
        if(qName.equals("Rel")){
            myRelFlag = true;
        }
        if(qName.equals("Title")){
            myTitleFlag = true;
            myID = attributes.getValue("ID");
        }
        if(qName.equals("Credit")){
            myCreditFlag = true;
        }
        if(qName.equals("name")){
            myCreditFlag = true;
        }
    }

    @Override
    public void endDocument () throws SAXException {

    }

    
}
