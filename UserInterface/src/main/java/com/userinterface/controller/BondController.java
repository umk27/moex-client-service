package com.userinterface.controller;

import com.userinterface.exceptions.BondServiceDoesNotExist;
import com.userinterface.feignclients.BondServiceClient;
import com.userinterface.model.Bond;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BondController {

    @Autowired
    BondServiceClient bondServiceClient;

    Logger logger = LoggerFactory.getLogger(BondController.class);

    @GetMapping("/add-bond-secid")
    public String addBondSecid() {
        return "add-bond-secid";
    }

    @GetMapping("/get-bond-info")
    @Retry(name = "bondServiceException", fallbackMethod = "bondServiceEx")
    public String getBondInfo(@RequestParam("secid/shortname") String str, Model model) {
        logger.info("Отправка запроса к сервису облигаций");
        Bond bond = null;
        try {
            bond = bondServiceClient.getShare(str.trim());
        } catch (RuntimeException e) {
            logger.error("Сервис облигаций не доступен");
            throw new BondServiceDoesNotExist("Сервис облигаций не доступен");
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
