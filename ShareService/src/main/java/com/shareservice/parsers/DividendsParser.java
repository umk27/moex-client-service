package com.shareservice.parsers;

import com.shareservice.exceptions.DividendsXMLParsingException;
import com.shareservice.model.Share;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
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
public class DividendsParser {

    private final static Logger logger = LoggerFactory.getLogger(DividendsParser.class);

    public List<Share.Dividends> parse(String dividendsXML) {

        List<Share.Dividends> dividendsList = new ArrayList<>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");

        try {
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            DocumentBuilder builder = factory.newDocumentBuilder();

            try (StringReader reader = new StringReader(dividendsXML)) {
                Document document = builder.parse(new InputSource(reader));
                document.getDocumentElement().normalize();

                NodeList list = document.getElementsByTagName("row");


                for (int i = 0; i < list.getLength(); i++) {
                    Element element = (Element) list.item(i);
                    String registryCloseDate = element.getAttribute("registryclosedate");
                    String value = element.getAttribute("value");

                    if (!registryCloseDate.isEmpty() && !value.isEmpty()) {
                        Share.Dividends dividends = new Share.Dividends();
                        dividends.setRegistryCloseDate(registryCloseDate);
                        dividends.setValue(Double.parseDouble(value));

                        dividendsList.add(dividends);
                    }
                }


            } catch (SAXException e) {
                logger.error("Ошибка парсигна XML файла дивидендов");
                throw new DividendsXMLParsingException("Ошибка парсигна XML файла дивидендов");
            } catch (IOException e) {
                logger.error("Ошибка парсигна XML файла дивидендов");
                throw new DividendsXMLParsingException("Ошибка парсигна XML файла дивидендов");
            }

        } catch (ParserConfigurationException e) {
            logger.error("Ошибка парсигна XML файла дивидендов");
            throw new DividendsXMLParsingException("Ошибка парсигна XML файла дивидендов");
        }
        return dividendsList;
    }
}
