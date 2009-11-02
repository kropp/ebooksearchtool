package org.ebooksearchtool.client.logic.parsing;

import org.ebooksearchtool.client.model.Settings;
import org.xml.sax.SAXException;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Created by IntelliJ IDEA.
 * User: 
 * Date: 25.10.2009
 * Time: 17:53:27
 * To change this template use File | Settings | File Templates.
 */
public class SAXSetHandler extends DefaultHandler{

    private Settings mySettings;
    private SettingTags myTags = new SettingTags();

    public SAXSetHandler(Settings settings){

        mySettings = settings;

    }

    public Settings getSettings()
    {
        return mySettings;
    }
    @Override
    public void startDocument() throws SAXException
    {
    }
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
    {

        if(qName.equals("proxy")){
            mySettings.setProxyEnabled(attributes.getValue("enabled").equals("true"));
        }
        for(int i = 0; i < myTags.getTags().length; ++i){
            myTags.getTags()[i].setStatus(myTags.getTags()[i].getName().equals(qName));

        }
    }
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException
    {
            for(int i = 0; i < myTags.getTags().length; ++i){
                if(myTags.getTags()[i].getStatus()) {
                        if(myTags.getTags()[i].getName().equals("server")){
                        	mySettings.setServer(new String(ch, start, length));
                        }else if(myTags.getTags()[i].getName().equals("IP")){
                        	mySettings.setIP(new String(ch, start, length));
                        }else if(myTags.getTags()[i].getName().equals("port")){
                        	mySettings.setPort(Integer.parseInt(new String(ch, start, length)));
                        }
                    }
        }
    }
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException
    {
        for(int i = 0; i < myTags.getTags().length; ++i){
            myTags.getTags()[i].setStatus(false);
        }
    }
    @Override
    public void endDocument() throws SAXException
    {
    }


}
