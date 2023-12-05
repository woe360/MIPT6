package com.example.mipt6;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import java.io.StringReader;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.xml.sax.InputSource;

public class Parser {
    public static ArrayList<String> parseXml(String xmlData) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(xmlData)));

            ArrayList<String> result = new ArrayList<>();

            NodeList cubeList = document.getElementsByTagName("Cube");
            for (int i = 0; i < cubeList.getLength(); i++) {
                Element cubeElement = (Element) cubeList.item(i);
                if (cubeElement.hasAttribute("currency") && cubeElement.hasAttribute("rate")) {
                    String currencyCode = cubeElement.getAttribute("currency");
                    String rate = cubeElement.getAttribute("rate");
                    result.add(currencyCode + " - " + rate);
                }
            }
            return result;


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}