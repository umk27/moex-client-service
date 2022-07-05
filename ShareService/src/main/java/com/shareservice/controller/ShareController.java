package com.shareservice.controller;

import com.shareservice.exceptions.*;
import com.shareservice.model.Share;
import com.shareservice.services.GetLogsService;
import com.shareservice.services.ShareService;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShareController {

    @Autowired
    ShareService shareService;

    @Autowired
    GetLogsService getLogsService;

    @GetMapping("getShare/secid/{secid}")
    @Retry(name = "share-exception", fallbackMethod = "shareEx")
    public Share getShare(@PathVariable String secid) {
        // LKOH
        // YNDX
        // GAZP

        Share share = shareService.getShare(secid);

        // shareService.getDividends("LKOH");

        return share;
    }

    @GetMapping("getLogs")
    @Retry(name = "logs -exception", fallbackMethod = "logsEx")
    public String getLogs() {
        return getLogsService.getLogs();
    }

    public Share shareEx(String secid, ShareNotFoundException exception) {
        Share share = new Share();
        share.setError("Акция " + secid + " не найдена на " +
                "Московской бирже");
        return share;
    }

    public Share shareEx(String secid, ShareLimitsRequestsException exception) {
        Share share = new Share();
        share.setError("Мосбиржа не отвечает на запрос " +
                "о получении информации об акциях");
        return share;
    }

    public Share shareEx(String secid, DividendsLimitsRequestsException exception) {
        Share share = new Share();
        share.setError("Мосбиржа не отвечает на запрос " +
                " о получении информации о дивидендах");
        return share;
    }

    public Share shareEx(String secid, ShareXMLParsingException exception) {
        Share share = new Share();
        share.setError("Ошибка парсинга XML файла акций");
        return share;
    }

    public Share shareEx(String secid, DividendsXMLParsingException exception) {
        Share share = new Share();
        share.setError("Ошибка парсинга XML файла дивидендов");
        return share;
    }

    public String logsEx(GetLogsException exception){
        return "Ошибка получения логов";
    }
}
