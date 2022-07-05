package com.bondservice.moexclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "corporateBondClient", url = "https://iss.moex.com",
        configuration = FeignConfig.class)
public interface CorporateBondClient {

    @GetMapping("iss/engines/stock/markets/bonds/boards/TQCB/securities.xml?iss.meta=off&iss.only=securities")
    String getCorporateBonds();
}
