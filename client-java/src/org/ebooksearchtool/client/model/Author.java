package org.ebooksearchtool.client.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Администратор
 * Date: 26.10.2009
 * Time: 21:17:48
 * To change this template use File | Settings | File Templates.
 */
public class Author {

    private String myName;
    private String myID;
    private List<Book> myBooks;

    public Author(){}

    public String getName() {
        return myName;
    }

    public void setName(String name) {
        myName = name;
    }

    public String getID() {
        return myID;
    }

    public void setID(String ID) {
        myID = ID;
    }

    public void addBook(Book newBook){
        myBooks.add(newBook);
    }

    public List<Book> getBooks(){
        return Collections.unmodifiableList(myBooks);
    }

}
