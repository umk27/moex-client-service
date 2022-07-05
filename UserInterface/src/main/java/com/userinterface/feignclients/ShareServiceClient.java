package com.userinterface.feignclients;

import com.userinterface.model.Share;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "shareServiceClient", url = "http://localhost:8080",
        configuration = FeignConfig.class)
public interface ShareServiceClient {

    @GetMapping("getShare/secid/{secid}")
    Share getShare(@PathVariable("secid") String secid);

    @GetMapping("getLogs")
    String getLogs();

}
