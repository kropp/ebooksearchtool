package org.ebooksearchtool.client.logic.parsing;

/**
 * Created by IntelliJ IDEA.
 * User: �������������
 * Date: 25.10.2009
 * Time: 17:59:47
 * To change this template use File | Settings | File Templates.
 */
public class SettingTags {

    private Tag[] myTags;

    public SettingTags(){
        myTags = new Tag[2];
        myTags[0] = new Tag("IP");
        myTags[1] = new Tag("port");
    }

    public Tag[] getTags(){
        return myTags;
    }

}