package ru.podolsky.stringparser;

import ru.podolsky.stringparser.tests.TestBuilder;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import ru.podolsky.stringparser.algorithms.AuthorsSimpleParser;
import ru.podolsky.stringparser.tests.Tester;

/**
 *
 * @author Алексей
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            //TestBuilder.build(new File("munsey.xml"));
            Tester.Test(AuthorsSimpleParser.Instance());
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
