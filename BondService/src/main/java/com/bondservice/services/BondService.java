package com.bondservice.services;

import com.bondservice.exceptions.BondNotFoundException;
import com.bondservice.exceptions.CouponLimitRequestsException;
import com.bondservice.model.Bond;
import com.bondservice.moexclient.CouponClient;
import com.bondservice.parsers.CouponParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class BondService {

    Logger logger = LoggerFactory.getLogger(BondService.class);

    @Autowired
    private BondRepository bondRepository;

    @Autowired
    private CouponClient couponClient;

    @Autowired
    private CouponParser couponParser;

    @Autowired
    private CouponIncomeCounter couponIncomeCounter;

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


}
