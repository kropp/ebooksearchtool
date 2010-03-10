package org.ebooksearchtool.client.logic.parsing;

/* Date: 01.10.2009
 * Time: 20:32:55
 * To change this template use File | Settings | File Templates.
 */
public class BookTags {

    private Tag[] myTags;

    public BookTags(){
        myTags = new Tag[14];
        myTags[0] = new Tag("title");
        myTags[1] = new Tag("id");
        myTags[2] = new Tag("author");
        myTags[3] = new Tag("language");
        myTags[4] = new Tag("issued");
        myTags[5] = new Tag("category");
        myTags[6] = new Tag("summary");
        myTags[7] = new Tag("content");
        myTags[8] = new Tag("link");
        myTags[9] = new Tag("updated");
        myTags[10] = new Tag("rights");
        myTags[11] = new Tag("publisher");
        myTags[12] = new Tag("subtitle");
        myTags[13] = new Tag("sourceServer");
    }

    public Tag[] getTags(){
        return myTags;
    }

}
