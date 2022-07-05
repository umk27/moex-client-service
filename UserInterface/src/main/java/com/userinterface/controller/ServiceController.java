package com.userinterface.controller;

import com.userinterface.feignclients.BondServiceClient;
import com.userinterface.feignclients.ShareServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServiceController {

    @Autowired
    ShareServiceClient shareServiceClient;

    @Autowired
    BondServiceClient bondServiceClient;

    @GetMapping("get-share-service-logs")
    public String getShareServiceLogs(){
        return shareServiceClient.getLogs();
    }

    @GetMapping("get-bond-service-logs")
    public String getBondServiceLogs(){
        return bondServiceClient.getLogs();
    }
}
