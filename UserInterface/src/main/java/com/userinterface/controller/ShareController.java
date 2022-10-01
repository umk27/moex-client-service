package com.userinterface.controller;

import com.google.gson.Gson;
import com.userinterface.exceptions.ShareServiceDoesNotExist;
import com.userinterface.kafka.ShareServiceProducer;
import com.userinterface.model.Share;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Instant;


@Controller
public class ShareController {

    private final ShareServiceProducer shareServiceProducer;

    private final Gson jsonConverter;

    private Share share;


    public ShareController(ShareServiceProducer shareServiceProducer, Gson jsonConverter) {
        this.shareServiceProducer = shareServiceProducer;
        this.jsonConverter = jsonConverter;
    }

    public void setShare(Share share) {
        this.share = share;
    }

    private static final Logger logger = LoggerFactory.getLogger(ShareController.class);

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
        share = null;
        logger.info("Отправка запроса к сервису акций");

        shareServiceProducer.sendMessage(str);

        Instant now = Instant.now();
        while (share == null) {
            logger.info("Ожидание ответа сервиса акций");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Instant time = Instant.now();
            if (time.toEpochMilli() - now.toEpochMilli() > 10000) {
                logger.error("Сервис акций не доступен");
                throw new ShareServiceDoesNotExist("Сервис акций не доступен");
            }
        }

        logger.info("Ответ сервиса акций получен");
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
