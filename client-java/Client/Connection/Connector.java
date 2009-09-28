package Connection;

import java.net.URL;
import java.net.URLConnection;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 22.09.2009
 * Time: 20:38:21
 * To change this template use File | Settings | File Templates.
 */
public class Connector{

    URL Url;

    public Connector(String adress) throws IOException {
        Url = new URL(adress);
    }

    public void GetFileFromURL() throws IOException {

        URLConnection connection;
        connection = Url.openConnection();

        PrintWriter pw = new PrintWriter   // класс с методами записи строк
            (new OutputStreamWriter          // класс-преобразователь
            (new FileOutputStream         // класс записи байтов в файл
            ("file.xml"), "utf-8"));

        int i = 0;
        while (i != connection.getContentLength()){
            pw.print((char)connection.getInputStream().read());
            ++i;
        }
        pw.close();

    }

}
