package com.userinterface.controller;

import com.userinterface.exceptions.BondServiceDoesNotExist;
import com.userinterface.exceptions.ShareServiceDoesNotExist;
import com.userinterface.feignclients.ShareServiceClient;
import com.userinterface.model.Share;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ShareController {

    @Autowired
    ShareServiceClient shareServiceClient;

    Logger logger = LoggerFactory.getLogger(ShareController.class);

    @GetMapping("/")
    public String getMainPage() {
        return "main-page";
    }

    @GetMapping("/add-share-secid")
    public String addShareSecid() {
        return "add-share-secid";
    }

    @GetMapping("/get-share-info")
    @Retry(name = "shareServiceException", fallbackMethod = "shareServiceEx")
    public String getShareInfo(@RequestParam("secid/shortname") String str, Model model) {
        logger.info("Отправка запроса к сервису акций");
        Share share = null;
        try {
            share = shareServiceClient.getShare(str.trim());
        } catch (RuntimeException e) {
            logger.error("Сервис акций не доступен");
            throw new ShareServiceDoesNotExist("Сервис акций не доступен");
        }

        logger.info("Ответ от сервиса акций получен");

        model.addAttribute("share", share);
        if (share.getMessage() != null) {
            return "show-share-info-message";
        }
        if (share.getError() != null) {
            return "show-share-info-error";
        }

        return "show-share-info";
    }

    public String shareServiceEx(String str, Model model, ShareServiceDoesNotExist exception) {
        Share share = new Share();
        share.setError("Сервис акций не доступен");
        model.addAttribute("share", share);
        return "show-share-info-error";
    }
}
