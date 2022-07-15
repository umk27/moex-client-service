package com.bondservice.controller;

import com.bondservice.exceptions.*;
import com.bondservice.model.Bond;
import com.bondservice.services.BondService;
import com.bondservice.services.GetLogsService;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class BondController {

    @Autowired
    private BondService bondService;

    @Autowired
    private GetLogsService getLogsService;

    @GetMapping("getBond/secid/{secid}")
    @Retry(name = "bond-exception", fallbackMethod = "bondEx")
    public Bond getBond(@PathVariable String secid) {

        return bondService.getBond(secid);
    }

    @GetMapping("getLogs")
    @Retry(name = "logs -exception", fallbackMethod = "logsEx")
    public String getLogs() {
        return getLogsService.getLogs();
    }

    public Bond bondEx(String secid, BondNotFoundException exception) {
        Bond bond = new Bond();
        bond.setError("Облигация " + secid + " не найдена на Московской бирже. " +
                "Проверьте правильность ввода SecId/ShortName");
        return bond;
    }

    public Bond bondEx(String secid, BondXMLParsingException exception) {
        Bond bond = new Bond();
        bond.setError("Ошибка парсигна XML файла облигаций");
        return bond;
    }

    public Bond bondEx(String secid, CouponXMLParsingException exception) {
        Bond bond = new Bond();
        bond.setError("Ошибка парсигна XML файла купонов");
        return bond;
    }

    public Bond bondEx(String secid, CorpBondLimitRequestsException exception) {
        Bond bond = new Bond();
        bond.setError("Мосбиржа не отвечает " +
                "на запрос данных о корпоративных облигациях");
        return bond;
    }

    public Bond bondEx(String secid, GovBondLimitRequestsException exception) {
        Bond bond = new Bond();
        bond.setError("Мосбиржа не отвечает " +
                "на запрос данных о государственных облигациях");
        return bond;
    }

    public Bond bondEx(String secid, CouponLimitRequestsException exception) {
        Bond bond = new Bond();
        bond.setError("Мосбиржа не отвечает " +
                "на запрос о получении данных о купонах облигации");
        return bond;
    }

    public String logsEx(GetLogsException exception){
        return "Ошибка получения логов";
    }
}
