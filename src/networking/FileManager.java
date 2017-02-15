package networking;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import ui.ImageViewWrapper;
import utils.CustomLogger;

/**
 * FileManager - class used for parsing XML into DOM and DOM structures into XML
 * files.
 *
 * @author Seitan
 */
public class FileManager {

    static Vector<String> parseXML(String httpResponse) {
        CustomLogger.logger.log(Level.INFO, "httpResponse: " + httpResponse);
        Document doc = convertStringToDocument(httpResponse);
        Vector<String> IDs;
        IDs = new Vector<String>();

        // each XML node entry should be a photo. caching the jpg and the metadata
        NodeList nodeList = doc.getElementsByTagName("entry");
        doc.getDocumentElement().normalize();

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nodeList.item(i);
                String phid = null;
                if (eElement.getElementsByTagName("id").getLength() == 1) {
                    String wholeId = eElement.getElementsByTagName("id").item(0).getTextContent();
                    phid = wholeId.substring(wholeId.lastIndexOf("/") + 1, wholeId.length() - 1);

                    CustomLogger.logger.log(Level.INFO, "Parsed id: " + phid);

                    IDs.add(phid);
                    writeMetaData(node.cloneNode(true), phid); //use the photo id to uniquely identify the photo metadata
                }
                //this return at least two nodes type link. check which one has type="image/jpeg"
                NodeList jpgs = eElement.getElementsByTagName("link");
                for (int k = 0; k < jpgs.getLength(); k++) {
                    Node nodeJPG = jpgs.item(k);
                    if (nodeJPG.getNodeType() == Node.ELEMENT_NODE) {
                        Element elemJPG = (Element) jpgs.item(k);
                        if (elemJPG.getAttribute("type") != null && elemJPG.getAttribute("type").contains("jpeg")) {
                            String parsedURL = elemJPG.getAttribute("href");
                            CustomLogger.logger.log(Level.INFO, "Parsed URL = " + parsedURL);
                            //download image
                            Network.downloadImage(parsedURL, "cache/" + phid + ".jpg");
                        }
                    }
                }
            }
        }
        return IDs;
    }

    private static void writeMetaData(Node n, String uniqueID) {
        File check = new File("cache/metadata/" + uniqueID + ".xml");
        if (check.exists()) {
            return;
        }

        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = null;
        try {
            docBuilder = docFactory.newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        Document saveDoc = docBuilder.newDocument();

        //manually add the "feed" node since the initial info it carried it not useful anymore
        Element rootElement = saveDoc.createElement("feed");
        saveDoc.appendChild(rootElement);

        //save full entry in case other info is needed to META
        if (n != null) {
            saveDoc.adoptNode(n);
            saveDoc.getDocumentElement().appendChild(n);
        }
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = transformerFactory.newTransformer();
        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        DOMSource source = new DOMSource(saveDoc);
        StreamResult result = new StreamResult(new File("cache/metadata/" + uniqueID + ".xml"));

        try {
            transformer.transform(source, result);
        } catch (TransformerException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static Document convertStringToDocument(String xmlStr) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(xmlStr)));
            return doc;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void fillData(String id, ImageViewWrapper viewWrapper) {
        File fXmlFile = new File("cache/metadata/" + id + ".xml");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;
        Document doc = null;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            doc = dBuilder.parse(fXmlFile);
        } catch (SAXException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        NodeList nodeList = doc.getElementsByTagName("entry");
        doc.getDocumentElement().normalize();

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nodeList.item(i);
                String phid = null;
                if (eElement.getElementsByTagName("title").getLength() == 1) {
                    String title = eElement.getElementsByTagName("title").item(0).getTextContent();
                    viewWrapper.setTitle(title);
                }
                if (eElement.getElementsByTagName("published").getLength() == 1) {
                    String publishDate = eElement.getElementsByTagName("published").item(0).getTextContent();
                    viewWrapper.setPublishDateFromString(publishDate);
                }
                if (eElement.getElementsByTagName("flickr:date_taken").getLength() == 1) {
                    String takenDate = eElement.getElementsByTagName("flickr:date_taken").item(0).getTextContent();
                    viewWrapper.setTakenDateFromString(takenDate);
                }
                NodeList jpgs = eElement.getElementsByTagName("link");
                for (int k = 0; k < jpgs.getLength(); k++) {
                    Node nodeJPG = jpgs.item(k);
                    if (nodeJPG.getNodeType() == Node.ELEMENT_NODE) {
                        Element elemJPG = (Element) jpgs.item(k);
                        if (elemJPG.getAttribute("type") != null && elemJPG.getAttribute("type").contains("jpeg")) {
                            viewWrapper.setURL(elemJPG.getAttribute("href"));
                        }
                    }
                }
            }
        }
    }
}
