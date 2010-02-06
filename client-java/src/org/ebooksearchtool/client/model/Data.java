package org.ebooksearchtool.client.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Data {

    private List<Book> myBooks = new ArrayList<Book>();
    private List<Author> myAuthors = new ArrayList<Author>();
    private String myNextPage = new String();
    private int myTotalBooksNumber;

    public Data(){}

    public void setNextPage(String adress){
    	myNextPage = adress;
    }
    
    public String getNextPage(){
    	return myNextPage;
    }
    
    public void setTotalBooksNumber(int number){
    	myTotalBooksNumber = number;
    }
    
    public int getTotalBooksNumber(){
    	return myTotalBooksNumber;
    }
    
    public void setBookTitle(int elementIndex, String value){
        myBooks.get(elementIndex).setTitle(value);
    }

    public void setBookAuthor(int elementIndex, Author value){
        myBooks.get(elementIndex).setAuthor(value);
    }

    public void setBookLanguage(int elementIndex, String value){
        myBooks.get(elementIndex).setLanguage(value);
    }

    public void setBookDate(int elementIndex, String value){
        myBooks.get(elementIndex).setDate(value);
    }

    public void setBookSummary(int elementIndex, String value){
        myBooks.get(elementIndex).setSummary(value);
    }

    public void setBookID(int elementIndex, String value){
        myBooks.get(elementIndex).setID(value);
    }

    /*public void setBookPdfLink(int elementIndex, String value){
        myBooks.get(elementIndex).setPdfLink(value);
    }

    public void setBookEpubLink(int elementIndex, String value){
        myBooks.get(elementIndex).setEpubLink(value);
    } */

    public Map<String, String> getBookLinks(int elementIndex){
        return myBooks.get(elementIndex).getLinks();
    }

    public void setBookImage(int elementIndex, String value){
        myBooks.get(elementIndex).setImage(value);
    }

    public void setBookGenre(int elementIndex, String value){
        myBooks.get(elementIndex).setGenre(value);
    }

    public void addBook(Book addition){
        myBooks.add(addition);
    }

    public void addAuthor(Author addition){
        myAuthors.add(addition);
    }

    public List<Book> getBooks() {
        return Collections.unmodifiableList(myBooks);
    }
    
    public List<Author> getAuthors() {
        return Collections.unmodifiableList(myAuthors);
    }

    public void setAuthorBook(int elementIndex, Book value){
        myAuthors.get(elementIndex).addBook(value);
    }

}
