package testconnection;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Aleksey Podolskiy
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            URL url = new URL("http://192.168.211.48:8000/data/insert");
            HttpURLConnection connect = (HttpURLConnection) url.openConnection();
            String message1="xml=%3C%3Fxml+version%3D%221.0%22+encoding%3D%22UTF-8%22%3F%3E%0D%0A%3Cbook%3E%0D%0A%3Ctitle%3EGrimms%27+Fairy+Tales%3C%2Ftitle%3E%0D%0A%3Clang%3Een%3C%2Flang%3E%0D%0A%3Cauthors%3E%0D%0A%3Cauthor%3E%0D%0A%3Cname%3EBrothers+Grimm%3C%2Fname%3E%0D%0A%3C%2Fauthor%3E%0D%0A%3C%2Fauthors%3E%0D%0A%3Cfiles%3E%0D%0A%3Cfile%3E%0D%0A%3Clink%3Ehttp%3A%2F%2Fwww.epubbooks.com%2Fbook%2F238%2Fgrimm-grimms-fairy-tales.epub%3C%2Flink%3E%0D%0A%3Csize%3E258048%3C%2Fsize%3E%0D%0A%3Ctype%3Eepub%3C%2Ftype%3E%0D%0A%0D%0A%0D%0A%0D%0A%3Cimg_link%3Ehttp%3A%2F%2Fwww.epubbooks.com%2Fimg-book-covers%2Fgrimm-grimms-fairy-tales-bookcover.jpg%3C%2Fimg_link%3E%0D%0A%3C%2Ffile%3E%0D%0A%3C%2Ffiles%3E%0D%0A%3Cannotation%3EDwarves+%2C+giants+%2C+princesses+%2C+kings+%2C+fairies+%2C+and+magicians+...+all+can+be+found+in+the+enchanting+fairy+tales+of+the+Brothers+Grimm.+Sixty-two+of+the+Grimms%27+best+stories+are+brought+to+life+%2C+from+well-known+favorites+like+%22Rapunzel+%2C+%22+%22Red+Riding+Hood+%2C+%22+%22Rumpelstiltskin+%2C+%22+and+%22Hansel+and+Grethel+%2C+%22+to+lesser-known+treasures+such+as+%22The+Valiant+Tailor%22+and+%22The+Frog+Prince.%22%3C%2Fannotation%3E%0D%0A%3C%2Fbook%3E";
            String message2="xml=%3C%3Fxml+version%3D%221.0%22+encoding%3D%22UTF-8%22%3F%3E%0D%0A%3Cbook%3E%0D%0A%3Ctitle%3EBlack+Beauty%3C%2Ftitle%3E%0D%0A%3Clang%3Een%3C%2Flang%3E%0D%0A%3Cauthors%3E%0D%0A%3Cauthor%3E%0D%0A%3Cname%3EAnna+Sewell%3C%2Fname%3E%0D%0A%3C%2Fauthor%3E%0D%0A%3C%2Fauthors%3E%0D%0A%3Cfiles%3E%0D%0A%3Cfile%3E%0D%0A%3Clink%3Ehttp%3A%2F%2Fwww.epubbooks.com%2Fbook%2F250%2Fsewell-black-beauty.epub%3C%2Flink%3E%0D%0A%3Csize%3E177152%3C%2Fsize%3E%0D%0A%3Ctype%3Eepub%3C%2Ftype%3E%0D%0A%0D%0A%0D%0A%0D%0A%3Cimg_link%3Ehttp%3A%2F%2Fwww.epubbooks.com%2Fimg-book-covers%2Fsewell-black-beauty-bookcover.jpg%3C%2Fimg_link%3E%0D%0A%3C%2Ffile%3E%0D%0A%3C%2Ffiles%3E%0D%0A%3Cannotation%3EBlack+Beauty+is+a+handsome+%2C+sweet-tempered+colt+with+a+strong+spirit.+As+a+young+colt+%2C+he+is+free+to+gallop+in+the+fresh+green+meadows+with+his+beloved+mother+%2C+Duchess+%2C+and+their+kind+master.+But+when+his+owners+are+forced+to+sell+him+%2C+Black+Beauty+goes+from+a+life+of+comfort+and+kindness+to+one+of+hard+labor+and+cruelty.+Bravely+he+works+as+hard+as+he+can+%2C+suffering+at+the+hands+of+men+who+treat+animals+badly.+But+Black+Beauty+has+an+unbreakable+spirit+and+will+%2C+and+is+determined+to+survive.%3C%2Fannotation%3E%0D%0A%3C%2Fbook%3E";
            connect.setDoInput(true);
            connect.setDoOutput(true);
            connect.setRequestMethod("POST");
            connect.setRequestProperty( "Content-type", "application/x-www-form-urlencoded" );
            connect.setRequestProperty( "Content-length", String.valueOf(getContentLength(message1)));
            OutputStream ops = connect.getOutputStream();
            PrintWriter pw = new PrintWriter(ops);

            pw.println(message1);
            pw.flush();

//            connect.setDoOutput(false);
            String s = "";
            BufferedReader is = new BufferedReader(new InputStreamReader(connect.getInputStream()));
            while(s.indexOf("</response>") == -1){
                s = is.readLine();
                System.out.println(s);
            }
            pw.close();
            ops.close();
            is.close();

            connect = (HttpURLConnection) url.openConnection();
            connect.setDoInput(true);
            connect.setDoOutput(true);
            connect.setRequestMethod("POST");
            connect.setRequestProperty( "Content-type", "application/x-www-form-urlencoded" );
            connect.setRequestProperty( "Content-length", String.valueOf(getContentLength(message2)));
            ops = connect.getOutputStream();
            pw = new PrintWriter(ops);

            pw.println(message2);
            pw.flush();

            s = "";
            is = new BufferedReader(new InputStreamReader(connect.getInputStream()));
            while(s.indexOf("</response>") == -1){
                s = is.readLine();
                System.out.println(s);
            }
            pw.close();
            ops.close();
            is.close();

//            BufferedWriter ostream = new BufferedWriter(new OutputStreamWriter(connect.getOutputStream()));
//            ostream.write(formatPost("%3C%3Fxml+version%3D%221.0%22+encoding%3D%22UTF-8%22%3F%3E%0D%0A%3Cbook%3E%0D%0A%3Ctitle%3EGrimms%27+Fairy+Tales%3C%2Ftitle%3E%0D%0A%3Clang%3Een%3C%2Flang%3E%0D%0A%3Cauthors%3E%0D%0A%3Cauthor%3E%0D%0A%3Cname%3EBrothers+Grimm%3C%2Fname%3E%0D%0A%3C%2Fauthor%3E%0D%0A%3C%2Fauthors%3E%0D%0A%3Cfiles%3E%0D%0A%3Cfile%3E%0D%0A%3Clink%3Ehttp%3A%2F%2Fwww.epubbooks.com%2Fbook%2F238%2Fgrimm-grimms-fairy-tales.epub%3C%2Flink%3E%0D%0A%3Csize%3E258048%3C%2Fsize%3E%0D%0A%3Ctype%3Eepub%3C%2Ftype%3E%0D%0A%0D%0A%0D%0A%0D%0A%3Cimg_link%3Ehttp%3A%2F%2Fwww.epubbooks.com%2Fimg-book-covers%2Fgrimm-grimms-fairy-tales-bookcover.jpg%3C%2Fimg_link%3E%0D%0A%3C%2Ffile%3E%0D%0A%3C%2Ffiles%3E%0D%0A%3Cannotation%3EDwarves+%2C+giants+%2C+princesses+%2C+kings+%2C+fairies+%2C+and+magicians+...+all+can+be+found+in+the+enchanting+fairy+tales+of+the+Brothers+Grimm.+Sixty-two+of+the+Grimms%27+best+stories+are+brought+to+life+%2C+from+well-known+favorites+like+%22Rapunzel+%2C+%22+%22Red+Riding+Hood+%2C+%22+%22Rumpelstiltskin+%2C+%22+and+%22Hansel+and+Grethel+%2C+%22+to+lesser-known+treasures+such+as+%22The+Valiant+Tailor%22+and+%22The+Frog+Prince.%22%3C%2Fannotation%3E%0D%0A%3C%2Fbook%3E"));
//            ostream.write(formatPost("%3C%3Fxml+version%3D%221.0%22+encoding%3D%22UTF-8%22%3F%3E%0D%0A%3Cbook%3E%0D%0A%3Ctitle%3EBlack+Beauty%3C%2Ftitle%3E%0D%0A%3Clang%3Een%3C%2Flang%3E%0D%0A%3Cauthors%3E%0D%0A%3Cauthor%3E%0D%0A%3Cname%3EAnna+Sewell%3C%2Fname%3E%0D%0A%3C%2Fauthor%3E%0D%0A%3C%2Fauthors%3E%0D%0A%3Cfiles%3E%0D%0A%3Cfile%3E%0D%0A%3Clink%3Ehttp%3A%2F%2Fwww.epubbooks.com%2Fbook%2F250%2Fsewell-black-beauty.epub%3C%2Flink%3E%0D%0A%3Csize%3E177152%3C%2Fsize%3E%0D%0A%3Ctype%3Eepub%3C%2Ftype%3E%0D%0A%0D%0A%0D%0A%0D%0A%3Cimg_link%3Ehttp%3A%2F%2Fwww.epubbooks.com%2Fimg-book-covers%2Fsewell-black-beauty-bookcover.jpg%3C%2Fimg_link%3E%0D%0A%3C%2Ffile%3E%0D%0A%3C%2Ffiles%3E%0D%0A%3Cannotation%3EBlack+Beauty+is+a+handsome+%2C+sweet-tempered+colt+with+a+strong+spirit.+As+a+young+colt+%2C+he+is+free+to+gallop+in+the+fresh+green+meadows+with+his+beloved+mother+%2C+Duchess+%2C+and+their+kind+master.+But+when+his+owners+are+forced+to+sell+him+%2C+Black+Beauty+goes+from+a+life+of+comfort+and+kindness+to+one+of+hard+labor+and+cruelty.+Bravely+he+works+as+hard+as+he+can+%2C+suffering+at+the+hands+of+men+who+treat+animals+badly.+But+Black+Beauty+has+an+unbreakable+spirit+and+will+%2C+and+is+determined+to+survive.%3C%2Fannotation%3E%0D%0A%3C%2Fbook%3E"));
//            ostream.close();
        } catch (IOException ex) {
            try {
                new BufferedWriter(new FileWriter(new File("ffd.txt"))).write(ex.getMessage(), 0, 1000);
            } catch (IOException ex1) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }

    private static String formatPost(String message){
        StringBuilder str = new StringBuilder();
        str.append("POST " + "/data/insert " + "HTTP/1.0");
        str.append(AnalyzerProperties.getPropertie("systemSeparator"));
        str.append("Host: " + AnalyzerProperties.getPropertie("serverAddress") +
                ":" + AnalyzerProperties.getPropertie("serverPort"));
        str.append(AnalyzerProperties.getPropertie("systemSeparator"));
        str.append("Content-Type: application/x-www-form-urlencoded");
        str.append(AnalyzerProperties.getPropertie("systemSeparator"));
        str.append("Content-Length: " + getContentLength(message));
        str.append(AnalyzerProperties.getPropertie("systemSeparator"));
        str.append(AnalyzerProperties.getPropertie("systemSeparator"));
        str.append("xml=" + message);
        str.append(AnalyzerProperties.getPropertie("systemSeparator"));
        str.append(AnalyzerProperties.getPropertie("systemSeparator"));

        System.out.println(str.toString());
        return str.toString();
    }

    private static long getContentLength(String str){
        return str.length();
    }

}
