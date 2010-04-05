package org.ebooksearchtool.analyzer.algorithms.subalgorithms;

import org.ebooksearchtool.analyzer.utils.AlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.ebooksearchtool.analyzer.model.Author;
import org.ebooksearchtool.analyzer.model.BookInfo;
import org.ebooksearchtool.analyzer.model.File;
import org.ebooksearchtool.analyzer.model.Language;
import org.ebooksearchtool.analyzer.model.Lexema;
import org.ebooksearchtool.analyzer.model.Title;

/**
 * @author Aleksey Podolskiy
 */

public class InfoExtractor {
    //TODO:Types for BookInfo. Later here we can add smart alghoritms in "is"
    //methods to search info.
    /**
     * Enum to differentiate types of info in analyze process.
     */
    public enum InfoType{
        title,
        author,
        bookCover,
        language;
    }

    /**
     * Extract information about titles, authors and other book info and
     * returned all combinations of that infos in <b>BookInfo</b> list with
     * credit indexes respectively to metrics, cheks and internal modifiers.
     * @param lexems list of <b>Lexems</b> to analyze
     * @return List of <b>BookInfo</b>
     */
//    public static List<BookInfo> extract(List<Lexema> lexems) throws AlgorithmException{
//        ArrayList<BookInfo> out = new ArrayList<BookInfo>();
//        HashMap<Title, Integer> titles = new HashMap<Title, Integer>();
//        HashMap<Author, Integer> authors = new HashMap<Author, Integer>();
//        HashMap<String, Integer> bookCovers = new HashMap<String, Integer>();
//        HashMap<Language, Integer> languages = new HashMap<Language, Integer>();
//        HashMap<String, Integer> annotations = new HashMap<String, Integer>();
//        File file;
//
//        file = FileInfoExtaractor.extractFileInfo(lexems);
//
//        //Intrgrer in Map is mid distation between all information in bookInfo
//        //need to calculate thems.
//        int length = lexems.size();
//        int chekCollision;
//        Title title;
//        List<Author> author;
//        String bookCover;
//        Language language;
//        String annotation;
//        for (int i = 0; i < length; i++) {
//            chekCollision = 0;
//            title = getTitle(lexems, i);
//            if(title.equals(null)){
//               chekCollision++;
//            }
//            author = getAuthor(lexems, i);
//            if(author.equals(null)){
//               chekCollision++;
//            }
//            bookCover = getBookCover(lexems, i);
//            if(bookCover.equals(null)){
//               chekCollision++;
//            }
//            language = getLanguage(lexems, i);
//            if(language.equals(null)){
//               chekCollision++;
//            }
//            annotation = getAnnotation(lexems, i);
//            if(annotation.equals(null)){
//               chekCollision++;
//            }
//            if(chekCollision > 1){
//                throw new AlgorithmException(title, author, bookCover, language, annotation);
//            }else{
//                if(title != null){
//                    titles.put(title, 0);
//                    break;
//                }
//                if(author != null){
//                    for(Author au:author){
//                        authors.put(au, 0);
//                    }
//                    break;
//                }
//                if(bookCover != null){
//                    bookCovers.put(bookCover, 0);
//                    break;
//                }
//                if(language != null){
//                    languages.put(language, 0);
//                    break;
//                }
//                if(annotation != null){
//                    annotations.put(annotation, 0);
//                    break;
//                }
//            }
//        }
//        //TODO:Составление BookInfo и вычисление их индексов.
//    }


    /**
     * Return title, if element on <b>pos</b> in <b>lexems</b> is title,
     * <b>null</b> otherwise.
     * @param lexems list of lexems to analyze
     * @param pos current position to analyze
     * @return <b>Title</b> if current element is title, <b>null</b> otherwise.
     */
    private static Title getTitle(List<Lexema> lexems, int pos){
        if(isTitle(lexems.get(pos).getValue())){
            int length = lexems.size();
            while(pos < length && !lexems.get(pos).isSentence() && !lexems.get(pos).isCloseTag()){
                pos++;
            }
            if(lexems.get(pos).isSentence()){
                return TitleParser.parse(lexems.get(pos).getValue());
            }else{
                return null;
            }
        }
        return null;
    }

    /**
     * Chek if string represents title.
     * @param info string to analyze
     * @return true if string is something looks like title, false otherwise.
     */
    public static boolean isTitle(String info){
        if(info.indexOf("title") >= 0){
            return true;
        }
        return false;
    }

    /**
     * Return author, if element on <b>pos</b> in <b>lexems</b> is author,
     * <b>null</b> otherwise.
     * @param lexems list of lexems to analyze
     * @param pos current position to analyze
     * @return <b>Author</b> if current element is author, <b>null</b> otherwise.
     */
    private static List<Author> getAuthor(List<Lexema> lexems, int pos){
        if(isAuthor(lexems.get(pos).getValue())){
            int length = lexems.size();
            while(pos < length && !lexems.get(pos).isSentence() && !lexems.get(pos).isCloseTag()){
                pos++;
            }
            if(lexems.get(pos).isSentence()){
                return AuthorsParser.parse(lexems.get(pos).getValue());
            }else{
                return null;
            }
        }
        return null;
    }

    /**
     * Chek if string represents title.
     * @param info string to analyze
     * @return true if string is something looks like title, false otherwise.
     */
    public static boolean isAuthor(String info){
        if(info.indexOf("author") >= 0){
            return true;
        }
        return false;
    }
}
