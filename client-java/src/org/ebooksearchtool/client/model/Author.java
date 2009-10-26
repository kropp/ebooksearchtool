package org.ebooksearchtool.client.model;

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

    public void setAuthor(String ID) {
        myID = ID;
    }

}
