package org.ebooksearchtool.client.model.books;

import java.util.*;

public class Data extends Observable {

    private List<Book> myBooks = new ArrayList<Book>();
    private List<Author> myAuthors = new ArrayList<Author>();


    public Data(){}
    
    public void setBookTitle(int elementIndex, String value){
        myBooks.get(elementIndex).setTitle(value);
    }

    public void setBookSubtitle(int elementIndex, String value){
        myBooks.get(elementIndex).setSubtitle(value);
    }

    public void setBookAuthor(int elementIndex, Author value){
        myBooks.get(elementIndex).setAuthor(value);
    }

    public void setBookLanguage(int elementIndex, String value){
        myBooks.get(elementIndex).setLanguage(value);
    }

    public void setBookPublisher(int elementIndex, String value){
        myBooks.get(elementIndex).setPublisher(value);
    }

    public void setBookRights(int elementIndex, String value){
        myBooks.get(elementIndex).setRights(value);
    }

    public void setBookDate(int elementIndex, String value){
        myBooks.get(elementIndex).setDate(value);
    }

    public void setBookUpdateTime(int elementIndex, String value){
        myBooks.get(elementIndex).setUpdateTime(value);
    }

    public void setBookSummary(int elementIndex, String value){
        myBooks.get(elementIndex).setSummary(value);
    }

    public void setBookContent(int elementIndex, String value){
        myBooks.get(elementIndex).setContent(value);
    }

    public void setBookID(int elementIndex, String value){
        myBooks.get(elementIndex).setID(value);
    }

    public Map<String, String> getBookLinks(int elementIndex){
        return myBooks.get(elementIndex).getLinks();
    }

    public void setBookImage(int elementIndex, String value){
        myBooks.get(elementIndex).setImage(value);
    }

    public void setBookGenre(int elementIndex, String value){
        myBooks.get(elementIndex).setGenre(value);
    }

    public void setBookSource(int elementIndex, String value){
        myBooks.get(elementIndex).setSource(value);
    }

    public void addBook(Book addition){
        myBooks.add(addition);
        setChanged();
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

}
