package org.ebooksearchtool.client.logic.parsing;

/**
 * Date: 25.10.2009
 * Time: 17:59:47
 */
public class SettingTags {

    private Tag[] myTags;

    public SettingTags(){
        myTags = new Tag[6];
        myTags[0] = new Tag("server");
        myTags[1] = new Tag("IP");
        myTags[2] = new Tag("port");
        myTags[3] = new Tag("proxy");
        myTags[4] = new Tag("pdfreader");
        myTags[5] = new Tag("epubreader");
    }

    public Tag[] getTags(){
        return myTags;
    }

}
