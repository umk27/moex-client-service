package com.shareservice.parsers;

import com.shareservice.exceptions.ShareXMLParsingException;
import com.shareservice.model.Share;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class ShareParser {

    Logger logger = LoggerFactory.getLogger(ShareParser.class);

    public List<Share> parse(String shareXML) {

        List<Share> shares = new ArrayList<>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");

        try {
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            DocumentBuilder builder = factory.newDocumentBuilder();

            try (StringReader reader = new StringReader(shareXML)) {
                Document document = builder.parse(new InputSource(reader));
                document.getDocumentElement().normalize();
                NodeList list = document.getElementsByTagName("row");

                for (int i = 0; i < list.getLength(); i++) {
                    Node node = list.item(i);


                    Element element = (Element) node;
                    String secId = element.getAttribute("SECID");
                    String shortName = element.getAttribute("SHORTNAME");
                    String prevPrice = element.getAttribute("PREVPRICE");
                    String lotSize = element.getAttribute("LOTSIZE");
                    String latName = element.getAttribute("LATNAME");


                    if (!secId.isEmpty() && !shortName.isEmpty() && !latName.isEmpty() && !prevPrice.isEmpty() && !lotSize.isEmpty()) {
                        Share share = new Share();
                        share.setSecId(secId);
                        share.setShortName(shortName);
                        share.setPrevPrice(Double.parseDouble(prevPrice));
                        share.setLotSize(Integer.parseInt(lotSize));
                        share.setLotPrice(new BigDecimal(prevPrice)
                                .multiply(new BigDecimal(lotSize)).doubleValue());
                        share.setLatName(latName);
                        shares.add(share);
                    }


                }

            } catch (SAXException e) {
                logger.error("Ошибка парсигна XML файла акций");
                throw new ShareXMLParsingException("Ошибка парсигна XML файла акций");
            } catch (IOException e) {
                logger.error("Ошибка парсигна XML файла акций");
                throw new ShareXMLParsingException("Ошибка парсигна XML файла акций");
            }

        } catch (ParserConfigurationException e) {
            logger.error("Ошибка парсигна XML файла акций");
            throw new ShareXMLParsingException("Ошибка парсигна XML файла акций");
        }

        return shares;
    }
}
