package com.userinterface.feignclients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "SHARE-SERVICE", url = "share-service:8081")
public interface ShareServiceClient {

    @GetMapping("getLogs")
    String getLogs();
}
