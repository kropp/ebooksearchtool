package exec;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Vector;

import Connection.Connector;
import Logic.Parser.Parser;
import Model.Data;

/**
 * Created by IntelliJ IDEA.
 * User: Администратор
 * Date: 01.10.2009
 * Time: 21:29:17
 * To change this template use File | Settings | File Templates.
 */
public class Controller {

    public static void main(String args[]) throws IOException, SAXException, ParserConfigurationException {

        Data books = new Data();

        Connector connect = new Connector("http://feedbooks.com/books/search.atom?query=Pushkin");

        connect.GetFileFromURL();

        Parser parser = new Parser();

        parser.parse("answer_file.xml", books);

    }

}
