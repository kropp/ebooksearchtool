package org.ebooksearchtool.client.utils;

import java.io.*;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.ebooksearchtool.client.model.books.Book;
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
            entry.appendChild(title);

            Element source = doc.createElement("sourceServer");
			source.setTextContent(data.getBooks().get(i).getSource());
            entry.appendChild(source);

            if (data.getBooks().get(i).getSubtitle() != null) {
                Element subtitle = doc.createElement("subtitle");
                subtitle.setTextContent(data.getBooks().get(i).getSubtitle());
                entry.appendChild(subtitle);
            }

            Element id = doc.createElement("id");
			id.setTextContent(data.getBooks().get(i).getID());
            entry.appendChild(id);

            if (data.getBooks().get(i).getAuthor() != null) {
                Element author = doc.createElement("author");
                Element name = doc.createElement("name");
                name.setTextContent(data.getBooks().get(i).getAuthor().getName());
                author.appendChild(name);
                if (data.getBooks().get(i).getAuthor().getID() != null) {
                    Element AID = doc.createElement("uri");
                    AID.setTextContent(data.getBooks().get(i).getAuthor().getID());
                    author.appendChild(AID);
                }
                entry.appendChild(author);
            }

            if (data.getBooks().get(i).getLanguage() != null) {
                Element language = doc.createElement("dcterms:language");
                language.setTextContent(data.getBooks().get(i).getLanguage());
                entry.appendChild(language);
            }

            if (data.getBooks().get(i).getPublisher() != null) {
                Element publisher = doc.createElement("publisher");
                publisher.setTextContent(data.getBooks().get(i).getPublisher());
                entry.appendChild(publisher);
            }

            if(data.getBooks().get(i).getDate() != null){
			    Element date = doc.createElement("dcterms:issued");
			    date.setTextContent(Integer.toString(data.getBooks().get(i).getDate().getYear()));
                entry.appendChild(date);
            }

            if(data.getBooks().get(i).getUpdateTime() != null){
			    Element update = doc.createElement("updated");
			    update.setTextContent(data.getBooks().get(i).getUpdateTime());
                entry.appendChild(update);
            }

            for(int j = 0; j < data.getBooks().get(i).getGenre().size(); ++j){
			    Element category = doc.createElement("category");
			    category.setAttribute("term", data.getBooks().get(i).getGenre().get(j));
                entry.appendChild(category);
            }

            if (data.getBooks().get(i).getRights() != null) {
                Element rights = doc.createElement("rights");
                rights.setTextContent(data.getBooks().get(i).getRights());
                entry.appendChild(rights);
            }

            if (data.getBooks().get(i).getSummary() != null) {
                Element summary = doc.createElement("summary");
                summary.setTextContent(data.getBooks().get(i).getSummary());
                entry.appendChild(summary);
            }

            if (data.getBooks().get(i).getContent() != null) {
                Element content = doc.createElement("content");
                content.setTextContent(data.getBooks().get(i).getContent());
                entry.appendChild(content);
            }

            if (data.getBooks().get(i).getLinks().get("epub") != null) {
                Element link1 = doc.createElement("link");
                link1.setAttribute("type", "application/epub+zip");
                link1.setAttribute("href", data.getBooks().get(i).getLinks().get("epub"));
                entry.appendChild(link1);
            }

            if (data.getBooks().get(i).getLinks().get("pdf") != null) {
                Element link2 = doc.createElement("link");
                link2.setAttribute("type", "application/pdf");
                link2.setAttribute("href", data.getBooks().get(i).getLinks().get("pdf"));
                entry.appendChild(link2);
            }

            if (data.getBooks().get(i).getImage() != null) {
                Element linkCov = doc.createElement("link");
                linkCov.setAttribute("type", "image/png");
                linkCov.setAttribute("rel", "http://opds-spec.org/thumbnail");
                linkCov.setAttribute("href", data.getBooks().get(i).getImage());
                entry.appendChild(linkCov);
            }

			root.appendChild(entry);
		}
		
		writeFile(doc, fileName);

    }

    public void makeSettingsXML(Settings sets){

        Document doc = myDomBuilder.newDocument();
		Element root = doc.createElement("settings");
        doc.appendChild(root);

        String[] servers = sets.getSupportedServers().keySet().toArray(new String[sets.getSupportedServers().size()]);
        for (int i = 0; i < sets.getSupportedServers().size(); ++i) {
            Element server = doc.createElement("server");
            server.setAttribute("name", servers[i]);
            server.setAttribute("searchTerm", sets.getSupportedServers().get(servers[i]).getSearchTerms());
            server.setAttribute("enabled", "" + sets.getSupportedServers().get(servers[i]).isEnabled());
            root.appendChild(server);
        }

        Element proxy = doc.createElement("proxy");
        proxy.setAttribute("enabled", "" + sets.isProxyEnabled());
        root.appendChild(proxy);

        Element IP = doc.createElement("IP");
        IP.setTextContent(sets.getIP());
        root.appendChild(IP);

        Element port = doc.createElement("port");
        port.setTextContent(((Integer)sets.getPort()).toString());
        root.appendChild(port);

        Element pdfReader = doc.createElement("pdfreader");
        pdfReader.setTextContent(sets.getPdfReader());
        root.appendChild(pdfReader);

        Element epubReader = doc.createElement("epubreader");
        epubReader.setTextContent(sets.getEpubReader());
        root.appendChild(epubReader);

        writeFile(doc, "settings.xml");

    }

}
