/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seniorresearch;

import java.io.File;
import java.io.StringWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;

/**
 *
 * @author derekgrove
 */
public interface XmlConversionMethods {

    public static String createMissionString() {
        final String xmlFilePath = "/Users/derekgrove/NetBeansProjects/SeniorResearch/SeniorResearch/src/seniorresearch/MainMission.xml";
        Document xmlDoc = convertXMLFileToXMLDocument(xmlFilePath);

        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer;
        String missionXmlString;
        try {
            transformer = tf.newTransformer();

            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(xmlDoc), new StreamResult(writer));
            missionXmlString = writer.getBuffer().toString();

        } catch (Exception e) {
            System.out.println("Conversion from xml to string errored out");
            missionXmlString = null;
        }

        return missionXmlString;
    }

    static Document convertXMLFileToXMLDocument(String filePath) {
        //Parser that produces DOM object trees from XML content
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        //API to obtain DOM Document instance
        DocumentBuilder builder = null;
        try {
            //Create DocumentBuilder with default configuration
            builder = factory.newDocumentBuilder();

            //Parse the content to Document object
            Document xmlDocument = builder.parse(new File(filePath));

            return xmlDocument;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
