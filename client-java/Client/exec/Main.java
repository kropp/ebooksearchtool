package exec;

import Connection.Connector;
import Logic.Parser.Parser;

import java.io.IOException;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 22.09.2009
 * Time: 19:27:07
 * To change this template use File | Settings | File Templates.
 */
public class Main {

    public static void main(String args[]) throws IOException, SAXException, ParserConfigurationException {

        Connector connect = new Connector("http://feedbooks.com/books");

        connect.GetFileFromURL();

        Parser parser = new Parser();

//        parser.parse("file.xml");
        //TODO Handle with parsing

    }

}
