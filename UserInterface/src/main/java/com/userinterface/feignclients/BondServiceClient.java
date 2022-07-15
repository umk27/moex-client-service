package com.userinterface.feignclients;

import com.userinterface.model.Bond;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name= "BOND-SERVICE",
        configuration = FeignConfig.class)
public interface BondServiceClient {

    @GetMapping("getBond/secid/{secid}")
    Bond getShare(@PathVariable("secid") String secid);

    @GetMapping("getLogs")
    String getLogs();
}
