package org.ebooksearchtool.client.Logic.parsing;

/**
 * Created by IntelliJ IDEA.
 * User: Администратор
 * Date: 01.10.2009
 * Time: 20:32:55
 * To change this template use File | Settings | File Templates.
 */
public class BookTags {

    private Tag[] myTags;

    public BookTags(){
        myTags = new Tag[5];
        myTags[0] = new Tag("title");
        myTags[1] = new Tag("name");
        myTags[2] = new Tag("dcterms:language");
        myTags[3] = new Tag("dcterms:issued");
        myTags[4] = new Tag("summary");
    }

    public Tag[] getTags(){
        return myTags;
    }

}
