package com.bondservice.parsers;

import com.bondservice.exceptions.BondXMLParsingException;
import com.bondservice.model.Bond;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;


@Service
public class BondParser {

    private final static Logger logger = LoggerFactory.getLogger(BondParser.class);

    public List<Bond> parse(String bondXML) {

        List<Bond> bonds = new ArrayList<>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");

        try {
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            DocumentBuilder builder = factory.newDocumentBuilder();

            try (StringReader reader = new StringReader(bondXML)) {
                Document document = builder.parse(new InputSource(reader));
                document.getDocumentElement().normalize();
                NodeList list = document.getElementsByTagName("row");

                for (int i = 0; i < list.getLength(); i++) {
                    Node node = list.item(i);


                    Element element = (Element) node;
                    String secId = element.getAttribute("SECID");
                    String shortName = element.getAttribute("SHORTNAME");
                    String latName = element.getAttribute("LATNAME");
                    String prevPrice = element.getAttribute("PREVPRICE");

                    if (!secId.isEmpty() && !shortName.isEmpty() && !latName.isEmpty() && !prevPrice.isEmpty()) {
                        Bond bond = new Bond();
                        bond.setSecId(secId);
                        bond.setShortName(shortName);
                        bond.setLatName(latName);
                        bond.setPrevPrice(Double.parseDouble(prevPrice) * 10);
                        bonds.add(bond);
                    }


                }

            } catch (SAXException e) {
                logger.error("Ошибка парсигна XML файла облигаций");
                throw new BondXMLParsingException("Ошибка парсигна XML файла облигаций");
            } catch (IOException e) {
                logger.error("Ошибка парсигна XML файла облигаций");
                throw new BondXMLParsingException("Ошибка парсигна XML файла облигаций");
            }

        } catch (ParserConfigurationException e) {
            logger.error("Ошибка парсигна XML файла облигаций");
            throw new BondXMLParsingException("Ошибка парсигна XML файла облигаций");
        }

        return bonds;
    }
}
