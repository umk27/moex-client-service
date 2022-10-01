package com.userinterface.controller;

import com.google.gson.Gson;
import com.userinterface.exceptions.BondServiceDoesNotExist;
import com.userinterface.kafka.BondServiceProducer;
import com.userinterface.model.Bond;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Instant;

@Controller
public class BondController {

    private final static Logger logger = LoggerFactory.getLogger(BondController.class);

    private final BondServiceProducer bondServiceProducer;

    private final Gson jsonConverter;

    private Bond bond;

    public BondController(BondServiceProducer bondServiceProducer, Gson jsonConverter) {
        this.bondServiceProducer = bondServiceProducer;
        this.jsonConverter = jsonConverter;
    }

    public void setBond(Bond bond) {
        this.bond = bond;
    }

    @GetMapping("/add-bond-secid")
    public String addBondSecid() {
        return "add-bond-secid";
    }

    @GetMapping("/get-bond-info")
    public String getBondInfo(@RequestParam("secid/shortname") String str, Model model) {
        bond = null;
        logger.info("Отправка запроса к сервису облигаций");

        bondServiceProducer.sendMessage(str);

        Instant now = Instant.now();
        while (bond == null) {
            logger.info("Ожидание ответа сервиса облигаций");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Instant time = Instant.now();
            if (time.toEpochMilli() - now.toEpochMilli() > 10000) {
                logger.error("Сервис облигаций не доступен");
                throw new BondServiceDoesNotExist("Сервис облигаций не доступен");
            }
        }

        logger.info("Ответ от сервиса облигаций получен");

        model.addAttribute("bond", bond);
        if (bond.getMessage() != null) {
            return "show-bond-info-message";
        }
        if (bond.getError() != null) {
            return "show-bond-info-error";
        }

        return "show-bond-info";
    }

    public String bondServiceEx(String str, Model model, BondServiceDoesNotExist exception) {
        Bond bond = new Bond();
        bond.setError("Сервис облигаций не доступен");
        model.addAttribute("bond", bond);
        return "show-bond-info-error";
    }

}
