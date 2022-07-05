package com.bondservice.moexclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "governmentBondClient", url = "https://iss.moex.com",
        configuration = FeignConfig.class)
public interface GovernmentBondClient {

    @GetMapping("iss/engines/stock/markets/bonds/boards/TQOB/securities.xml?iss.meta=off&iss.only=securities")
    String getGovernmentBonds();
}
