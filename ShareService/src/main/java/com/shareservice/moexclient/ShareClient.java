package com.shareservice.moexclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "shareClient", url = "https://iss.moex.com",
        configuration = FeignConfig.class)
public interface ShareClient {

    @GetMapping("/iss/engines/stock/markets/shares/boards/TQBR/securities.xml?iss.only=securities&iss.meta=off")
    String getShares();
}
