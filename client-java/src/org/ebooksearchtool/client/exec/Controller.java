package org.ebooksearchtool.client.exec;

import org.xml.sax.SAXException;
import org.ebooksearchtool.client.View.Viewer;
import org.ebooksearchtool.client.Model.Data;
import org.ebooksearchtool.client.Connection.Connector;
import org.ebooksearchtool.client.Logic.parsing.*;
import org.ebooksearchtool.client.Logic.Query.*;


import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;



/**
 * Created by IntelliJ IDEA.
 * User: 
 * Date: 01.10.2009
 * Time: 21:29:17
 * To change this template use File | Settings | File Templates.
 */
public class Controller {
	
	Viewer viewer;

    public static void main(String args[]) throws IOException, SAXException, ParserConfigurationException {

        Data books = new Data();
        
        Query query = new Query();
        
        String adress = query.getQueryAdress();

        Connector connect = new Connector(adress);

        connect.GetFileFromURL();

        Parser parser = new Parser();

        parser.parse("answer_file.xml", books);

    }

}
