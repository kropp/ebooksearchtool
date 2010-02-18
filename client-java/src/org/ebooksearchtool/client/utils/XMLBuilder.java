package org.ebooksearchtool.client.utils;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.ebooksearchtool.client.model.books.Data;
import org.ebooksearchtool.client.model.settings.Settings;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XMLBuilder {
	
	private DocumentBuilder myDomBuilder;
	
	public XMLBuilder() {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			myDomBuilder = factory.newDocumentBuilder();	
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	private void writeFile(Document doc, String fileName){
		try {
			Transformer t = TransformerFactory.newInstance().newTransformer();
			t.setOutputProperty(OutputKeys.INDENT, "yes");
			
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileName), "utf-8"));
			
			t.transform(new DOMSource(doc), new StreamResult(pw));
			pw.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void makeXML(Data data, String fileName){

		Document doc = myDomBuilder.newDocument();
		Element root = doc.createElement("feed");
        root.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:opensearch", "http://a9.com/-/spec/opensearch/1.1/");
		root.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:dcterms", "http://purl.org/dc/terms/");
        doc.appendChild(root);
		
		for(int i = 0; i < data.getBooks().size(); ++i){
			Element entry = doc.createElement("entry");
			Element title = doc.createElement("title");
			title.setTextContent(data.getBooks().get(i).getTitle());
			Element id = doc.createElement("id");
			id.setTextContent(data.getBooks().get(i).getID());
			Element author = doc.createElement("author");
			Element name = doc.createElement("name");
			name.setTextContent(data.getBooks().get(i).getAuthor().getName());
			Element AID = doc.createElement("uri");
			AID.setTextContent(data.getBooks().get(i).getAuthor().getID());
			Element language = doc.createElement("dcterms:language");
			language.setTextContent(data.getBooks().get(i).getLanguage());
            if(data.getBooks().get(i).getDate() != null){
			    Element date = doc.createElement("dcterms:issued");
			    date.setTextContent(Integer.toString(data.getBooks().get(i).getDate().getYear()));
                entry.appendChild(date);
            }
            for(int j = 0; j < data.getBooks().get(i).getGenre().size(); ++j){
			    Element category = doc.createElement("category");
			    category.setAttribute("term", data.getBooks().get(i).getGenre().get(j));
                entry.appendChild(category);
            }
			Element summary = doc.createElement("summary");
			summary.setTextContent(data.getBooks().get(i).getSummary());
			Element link1 = doc.createElement("link");
			link1.setAttribute("type", "application/epub+zip");
			link1.setAttribute("href", data.getBooks().get(i).getLinks().get("epub"));
			Element link2 = doc.createElement("link");
			link2.setAttribute("type", "application/pdf");
			link2.setAttribute("href", data.getBooks().get(i).getLinks().get("pdf"));
			Element linkCov = doc.createElement("link");
			linkCov.setAttribute("type", "image/png");
            linkCov.setAttribute("rel", "http://opds-spec.org/thumbnail");
			linkCov.setAttribute("href", data.getBooks().get(i).getImage());
			
			entry.appendChild(title);
			entry.appendChild(id);
			author.appendChild(name);
			author.appendChild(AID);
			entry.appendChild(author);
			entry.appendChild(language);
			entry.appendChild(summary);
			entry.appendChild(link1);
			entry.appendChild(link2);
			entry.appendChild(linkCov);
			
			root.appendChild(entry);
		}
		
		writeFile(doc, fileName);
		
	}

    public void makeSettingsXML(Settings sets){

        Document doc = myDomBuilder.newDocument();
		Element root = doc.createElement("settings");
        doc.appendChild(root);

        Element server = doc.createElement("server");
        server.setTextContent(sets.getServer());
        root.appendChild(server);

        Element proxy = doc.createElement("proxy");
        proxy.setAttribute("enabled", "" + sets.isProxyEnabled());
        root.appendChild(proxy);

        Element IP = doc.createElement("IP");
        IP.setTextContent(sets.getIP());
        root.appendChild(IP);

        Element port = doc.createElement("port");
        port.setTextContent(((Integer)sets.getPort()).toString());
        root.appendChild(port);

        Set<String> keySet = sets.getSupportedServers().keySet();
        Object[] keys = keySet.toArray();
        for(int i = 0; i < keys.length; ++i){
            Element ser = doc.createElement("supported");
            ser.setAttribute("search", sets.getSupportedServers().get(keys[i]));
            ser.setAttribute("server", (String)keys[i]);
            root.appendChild(ser);
        }

        writeFile(doc, "settings.xml");

    }

}
