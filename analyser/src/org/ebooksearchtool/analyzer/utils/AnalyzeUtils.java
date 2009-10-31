package org.ebooksearchtool.analyzer.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.ebooksearchtool.analyzer.model.*;

/**
 * @author Алексей
 */

public class AnalyzeUtils {
    private static HashSet myLanguages = new HashSet();
    static{
       myLanguages.addAll(createLanguageList());
    }

    public static boolean isLanguage(String value){
        if(myLanguages.contains(value)){
            return true;
        }else{
            return false;
        }
    }

    public static String bookInfoToString(BookInfo info){
       StringBuilder sb = new StringBuilder();

       sb.append("Title: " + info.getTitle() + AnalyzerProperties.getPropertie("systemSeparator"));
       List<Author> authors = info.getAuthors();
       int length = authors.size();
       sb.append("Authors: " + AnalyzerProperties.getPropertie("systemSeparator"));
       for (int i = 0; i < length; i++) {
           sb.append("    " + authors.get(i).getName() + AnalyzerProperties.getPropertie("systemSeparator"));
       }
       List<File> files = info.getFiles();
       length = files.size();
       sb.append("Files: " + AnalyzerProperties.getPropertie("systemSeparator"));
       for (int i = 0; i < length; i++) {
           sb.append("    Link: " + files.get(i).getLink() +"; " );
           sb.append("Size: " + files.get(i).getSize() +"; " );
           sb.append("Type: " + files.get(i).getType() + AnalyzerProperties.getPropertie("systemSeparator"));
       }
       sb.append("Language: " + info.getLanguage() + AnalyzerProperties.getPropertie("systemSeparator"));

       return sb.toString();
   }

    private static ArrayList<String> createLanguageList(){
        ArrayList<String> languages = new ArrayList<String>();
        languages.add("Afar");
        languages.add("Abkhazian");
        languages.add("Avestan");
        languages.add("Afrikaans");
        languages.add("Akan");
        languages.add("Amharic");
        languages.add("Aragonese");
        languages.add("Arabic");
        languages.add("Assamese");
        languages.add("Avaric");
        languages.add("Aymara");
        languages.add("Azerbaijani");
        languages.add("Bashkir");
        languages.add("Belarusian");
        languages.add("Bulgarian");
        languages.add("Bihari");
        languages.add("Bislama");
        languages.add("Bambara");
        languages.add("Bengali");
        languages.add("Tibetan");
        languages.add("Breton");
        languages.add("Bosnian");
        languages.add("Catalan");
        languages.add("Valencian");
        languages.add("Chechen");
        languages.add("Chamorro");
        languages.add("Corsican");
        languages.add("Cree");
        languages.add("Czech");
        languages.add("Church Slavic");
        languages.add("Old Slavonic");
        languages.add("Church Slavonic");
        languages.add("Old Bulgarian");
        languages.add("Old Church Slavonic");
        languages.add("Chuvash");
        languages.add("Welsh");
        languages.add("Danish");
        languages.add("German");
        languages.add("Divehi");
        languages.add("Dhivehi");
        languages.add("Maldivian");
        languages.add("Dzongkha");
        languages.add("Ewe");
        languages.add("Modern Greek");
        languages.add("English");
        languages.add("Esperanto");
        languages.add("Spanish");
        languages.add("Castilian");
        languages.add("Estonian");
        languages.add("Basque");
        languages.add("Persian");
        languages.add("Fulah");
        languages.add("Finnish");
        languages.add("Fijian");
        languages.add("Faroese");
        languages.add("French");
        languages.add("Western Frisian");
        languages.add("Irish");
        languages.add("Gaelic");
        languages.add("Scottish Gaelic");
        languages.add("Galician");
        languages.add("Guaraní");
        languages.add("Gujarati");
        languages.add("Manx");
        languages.add("Hausa");
        languages.add("Modern Hebrew");
        languages.add("Hindi");
        languages.add("Hiri Motu");
        languages.add("Croatian");
        languages.add("Haitian");
        languages.add("Haitian Creole");
        languages.add("Hungarian");
        languages.add("Armenian");
        languages.add("Herero");
        languages.add("Interlingua");
        languages.add("Indonesian");
        languages.add("Interlingue");
        languages.add("Occidental");
        languages.add("Igbo");
        languages.add("Sichuan Yi");
        languages.add("Nuosu");
        languages.add("Inupiaq");
        languages.add("Ido");
        languages.add("Icelandic");
        languages.add("Italian");
        languages.add("Inuktitut");
        languages.add("Japanese");
        languages.add("Javanese");
        languages.add("Georgian");
        languages.add("Kongo");
        languages.add("Kikuyu");
        languages.add("Gikuyu");
        languages.add("Kwanyama");
        languages.add("Kuanyama");
        languages.add("Kazakh");
        languages.add("Kalaallisut");
        languages.add("Greenlandic");
        languages.add("Central Khmer");
        languages.add("Kannada");
        languages.add("Korean");
        languages.add("Kanuri");
        languages.add("Kashmiri");
        languages.add("Kurdish");
        languages.add("Komi");
        languages.add("Cornish");
        languages.add("Kirghiz");
        languages.add("Kyrgyz");
        languages.add("Latin");
        languages.add("Luxembourgish");
        languages.add("Letzeburgesch");
        languages.add("Ganda");
        languages.add("Limburgish");
        languages.add("Limburgan");
        languages.add("Limburger");
        languages.add("Lingala");
        languages.add("Lao");
        languages.add("Lithuanian");
        languages.add("Luba-Katanga");
        languages.add("Latvian");
        languages.add("Malagasy");
        languages.add("Marshallese");
        languages.add("Māori");
        languages.add("Macedonian");
        languages.add("Malayalam");
        languages.add("Mongolian");
        languages.add("Marathi");
        languages.add("Malay");
        languages.add("Maltese");
        languages.add("Burmese");
        languages.add("Nauru");
        languages.add("Norwegian Bokmål");
        languages.add("North Ndebele");
        languages.add("Nepali");
        languages.add("Ndonga");
        languages.add("Dutch");
        languages.add("Flemish");
        languages.add("Norwegian Nynorsk");
        languages.add("Norwegian");
        languages.add("South Ndebele");
        languages.add("Navajo");
        languages.add("Navaho");
        languages.add("Chichewa");
        languages.add("Chewa");
        languages.add("Nyanja");
        languages.add("Occitan");
        languages.add("Ojibwa");
        languages.add("Oromo");
        languages.add("Oriya");
        languages.add("Ossetian");
        languages.add("Ossetic");
        languages.add("Panjabi");
        languages.add("Punjabi");
        languages.add("Pāli");
        languages.add("Polish");
        languages.add("Pashto");
        languages.add("Pushto");
        languages.add("Portuguese");
        languages.add("Quechua");
        languages.add("Romansh");
        languages.add("Rundi");
        languages.add("Romanian");
        languages.add("Moldavian");
        languages.add("Moldovan");
        languages.add("Russian");
        languages.add("Kinyarwanda");
        languages.add("Sanskrit");
        languages.add("Sardinian");
        languages.add("Sindhi");
        languages.add("Northern Sami");
        languages.add("Sango");
        languages.add("Sinhala");
        languages.add("Sinhalese");
        languages.add("Slovak");
        languages.add("Slovene");
        languages.add("Samoan");
        languages.add("Shona");
        languages.add("Somali");
        languages.add("Albanian");
        languages.add("Serbian");
        languages.add("Swati");
        languages.add("Southern Sotho");
        languages.add("Sundanese");
        languages.add("Swedish");
        languages.add("Swahili");
        languages.add("Tamil");
        languages.add("Telugu");
        languages.add("Tajik");
        languages.add("Thai");
        languages.add("Tigrinya");
        languages.add("Turkmen");
        languages.add("Tagalog");
        languages.add("Tswana");
        languages.add("Tonga");
        languages.add("Turkish");
        languages.add("Tsonga");
        languages.add("Tatar");
        languages.add("Twi");
        languages.add("Tahitian");
        languages.add("Uighur");
        languages.add("Uyghur");
        languages.add("Ukrainian");
        languages.add("Urdu");
        languages.add("Uzbek");
        languages.add("Venda");
        languages.add("Vietnamese");
        languages.add("Volapük");
        languages.add("Walloon");
        languages.add("Wolof");
        languages.add("Xhosa");
        languages.add("Yiddish");
        languages.add("Yoruba");
        languages.add("Zhuang");
        languages.add("Chuang");
        languages.add("Chinese");
        languages.add("Zulu");

        return languages;
   }
}
