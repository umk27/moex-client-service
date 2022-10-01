package com.userinterface.controller;


import com.userinterface.feignclients.BondServiceClient;
import com.userinterface.feignclients.ShareServiceClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

@RestController
public class ServiceController {

    private final BondServiceClient bondServiceClient;

    private final ShareServiceClient shareServiceClient;

    public ServiceController(BondServiceClient bondServiceClient, ShareServiceClient shareServiceClient) {
        this.bondServiceClient = bondServiceClient;
        this.shareServiceClient = shareServiceClient;
    }

    @GetMapping("get-share-service-logs")
    public String getShareServiceLogs() {
        return shareServiceClient.getLogs();
    }

    @GetMapping("get-bond-service-logs")
    public String getBondServiceLogs() {
        return bondServiceClient.getLogs();
    }

    @GetMapping("get-main-service-logs")
    public String getMainServiceLogs() {
        String logs = "";
        try (FileReader fileReader = new FileReader("logs/LogFile")) {
            Scanner scanner = new Scanner(fileReader);
            while (scanner.hasNextLine()) {
                logs = logs + scanner.nextLine() + "<br/>";
            }
        } catch (FileNotFoundException e) {
            return "Ошибка получения логов";
        } catch (IOException e) {
            return "Ошибка получения логов";
        }

        return logs;
    }
}
