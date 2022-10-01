package com.userinterface.feignclients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "BOND-SERVICE", url = "bond-service:8083")
public interface BondServiceClient {

    @GetMapping("getLogs")
    String getLogs();
}
