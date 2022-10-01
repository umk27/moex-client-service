package com.bondservice.controller;

import com.bondservice.exceptions.*;
import com.bondservice.services.GetLogsService;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class BondController {

    private final GetLogsService getLogsService;

    public BondController(GetLogsService getLogsService) {
        this.getLogsService = getLogsService;
    }

    @GetMapping("getLogs")
    @Retry(name = "logs -exception", fallbackMethod = "logsEx")
    public String getLogs() {
        return getLogsService.getLogs();
    }


    public String logsEx(GetLogsException exception){
        return "Ошибка получения логов";
    }
}
