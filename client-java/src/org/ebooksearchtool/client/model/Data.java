package org.ebooksearchtool.client.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: �������������
 * Date: 01.10.2009
 * Time: 20:08:37
 * To change this template use File | Settings | File Templates.
 */
public class Data {

    private String[] myAttributeNames = {
            "title",
            "author",
            "language",
            "date",
            "summary",
            "link"
    };
    private List<Book> myBooks = new ArrayList<Book>();
    private List<Author> myAuthors = new ArrayList<Author>();

    public Data(){}

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

    public void setBookLink(int elementIndex, String value){
        myBooks.get(elementIndex).setLink(value);
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

    public String[] getAttributes() {
        return myAttributeNames;
    }
}
