package com.shareservice.controller;

import com.shareservice.exceptions.*;
import com.shareservice.services.GetLogsService;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShareController {

    GetLogsService getLogsService;

    public ShareController(GetLogsService getLogsService) {
        this.getLogsService = getLogsService;
    }

    @GetMapping("getLogs")
    @Retry(name = "logs-exception", fallbackMethod = "logsEx")
    public String getLogs() {
        return getLogsService.getLogs();
    }

    public String logsEx(GetLogsException exception){
        return "Ошибка получения логов";
    }
}
