package org.ebooksearchtool.client.logic.parsing;

/**
 * Created by IntelliJ IDEA.
 * User: Администратор
 * Date: 29.10.2009
 * Time: 23:43:34
 * To change this template use File | Settings | File Templates.
 */
public class AuthorTags {

    private Tag[] myTags;

    public AuthorTags(){
        myTags = new Tag[2];
        myTags[0] = new Tag("name");
        myTags[1] = new Tag("uri");
    }

    public Tag[] getTags(){
        return myTags;
    }

}
