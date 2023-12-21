package com.bondservice.services;

import com.bondservice.exceptions.*;
import com.bondservice.model.Bond;
import com.bondservice.moexclient.CouponClient;
import com.bondservice.parsers.CouponParser;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j
public class BondService {

    private final static Logger logger = LoggerFactory.getLogger(BondService.class);

    private final BondRepository bondRepository;

    private final CouponClient couponClient;

    private final CouponParser couponParser;

    private final CouponIncomeCounter couponIncomeCounter;

    public BondService(BondRepository bondRepository, CouponClient couponClient, CouponParser couponParser, CouponIncomeCounter couponIncomeCounter) {
        this.bondRepository = bondRepository;
        this.couponClient = couponClient;
        this.couponParser = couponParser;
        this.couponIncomeCounter = couponIncomeCounter;
    }

    @Retry(name = "bond-exception", fallbackMethod = "bondEx")
    public Bond getBond(String str) {

        logger.info("Получение списка облигаций");

        List<Bond> bonds = bondRepository.getBonds();

        logger.info("Список облигаций успешно получен");

        Bond bond = null;
        for (Bond bond1 : bonds) {
            if (bond1.getSecId().equals(str) || bond1.getShortName().equals(str)) {
                bond = bond1;
            }
        }

        if (bond == null) {
            logger.error("Облигация " + str + " не найдена на " +
                    "Московской бирже");
            throw new BondNotFoundException("Облигация " + str + " не найдена на " +
                    "Московской бирже");
        }

        logger.info("Облигация " + str + " успешно найдена");

        logger.info("Получение списка купонов облигации " + str);

        String couponXML = "";
        try {
            couponXML = couponClient.getCoupons(bond.getSecId());
        } catch (RuntimeException e) {
            logger.error("Мосбиржа не отвечает " +
                    "на запрос о получении данных о купонах облигации");
            throw new CouponLimitRequestsException("Мосбиржа не отвечает " +
                    "на запрос о получении данных о купонах облигации");
        }


        List<Bond.Coupon> coupons = couponParser.parse(couponXML);

        bond.setCoupons(coupons);
        logger.info("Список купонов облигации " + str + " получен");

        logger.info("Рассчет купонного дохода");
        Bond bondResult = couponIncomeCounter.countCouponIncome(bond);
        logger.info("Купонный доход рассчитан");

        return bondResult;
    }

    public Bond bondEx(String str, BondNotFoundException exception) {
        Bond bond = new Bond();
        bond.setError("Облигация " + str + " не найдена на Московской бирже. " +
                "Проверьте правильность ввода SecId/ShortName");
        return bond;
    }

    public Bond bondEx(String str, BondXMLParsingException exception) {
        Bond bond = new Bond();
        bond.setError("Ошибка парсигна XML файла облигаций");
        return bond;
    }

    public Bond bondEx(String str, CouponXMLParsingException exception) {
        Bond bond = new Bond();
        bond.setError("Ошибка парсигна XML файла купонов");
        return bond;
    }

    public Bond bondEx(String str, CorpBondLimitRequestsException exception) {
        Bond bond = new Bond();
        bond.setError("Мосбиржа не отвечает " +
                "на запрос данных о корпоративных облигациях");
        return bond;
    }

    public Bond bondEx(String str, GovBondLimitRequestsException exception) {
        Bond bond = new Bond();
        bond.setError("Мосбиржа не отвечает " +
                "на запрос данных о государственных облигациях");
        return bond;
    }

    public Bond bondEx(String str, CouponLimitRequestsException exception) {
        Bond bond = new Bond();
        bond.setError("Мосбиржа не отвечает " +
                "на запрос о получении данных о купонах облигации");
        return bond;
    }

}
