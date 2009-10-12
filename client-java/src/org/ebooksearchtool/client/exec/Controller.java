package org.ebooksearchtool.client.exec;

import org.ebooksearchtool.client.connection.Connector;
import org.ebooksearchtool.client.logic.query.*;
import org.ebooksearchtool.client.logic.parsing.*;
import org.ebooksearchtool.client.model.Data;
import org.ebooksearchtool.client.view.Viewer;
import org.xml.sax.SAXException;

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

    Data books = new Data();
	
    public void act(String queryWord) throws IOException, SAXException, ParserConfigurationException {
        
        Query query = new Query();
        
        String adress = query.getQueryAdress(queryWord);

        Connector connect = new Connector(adress);

        connect.GetFileFromURL();

        Parser parser = new Parser();

        parser.parse("answer_file.xml", books);

    }

    public Data getData(){
        return books;
    }

}
