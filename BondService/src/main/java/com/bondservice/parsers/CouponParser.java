package com.bondservice.parsers;

import com.bondservice.exceptions.BondNotFoundException;
import com.bondservice.exceptions.CouponXMLParsingException;
import com.bondservice.model.Bond;
import com.bondservice.services.DateBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Service
public class CouponParser {

    Logger logger = LoggerFactory.getLogger(CouponParser.class);

    @Autowired
    private DateBuilder dateBuilder;

    public List<Bond.Coupon> parse(String couponXML) {

        List<Bond.Coupon> coupons = new ArrayList<>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");

        try {
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            DocumentBuilder builder = factory.newDocumentBuilder();

            try (StringReader reader = new StringReader(couponXML)) {
                Document document = builder.parse(new InputSource(reader));
                document.getDocumentElement().normalize();

                NodeList list = document.getElementsByTagName("row");

                for (int i = 0; i < list.getLength(); i++) {
                    Element element = (Element) list.item(i);
                    String couponDate = element.getAttribute("coupondate");
                    String value = element.getAttribute("value");
                    String valueprc = element.getAttribute("valueprc");

                    if (!couponDate.isEmpty() && !value.isEmpty() && !valueprc.isEmpty()) {
                        Bond.Coupon coupon = new Bond.Coupon();
                        coupon.setCouponDate(dateBuilder.build(couponDate));
                        coupon.setValue(Double.parseDouble(value));
                        coupon.setValueprc(Double.parseDouble(valueprc));

                        coupons.add(coupon);

                    }

                }

            } catch (SAXException e) {
                logger.error("Ошибка парсигна XML файла купонов");
                throw new CouponXMLParsingException("Ошибка парсигна XML файла купонов");
            } catch (IOException e) {
                logger.error("Ошибка парсигна XML файла купонов");
                throw new CouponXMLParsingException("Ошибка парсигна XML файла купонов");
            }

        } catch (ParserConfigurationException e) {
            logger.error("Ошибка парсигна XML файла купонов");
            throw new CouponXMLParsingException("Ошибка парсигна XML файла купонов");
        }
        return coupons;
    }


}
