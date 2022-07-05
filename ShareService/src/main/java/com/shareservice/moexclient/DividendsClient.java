package com.shareservice.moexclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "dividendsClient", url = "http://iss.moex.com",
        configuration = FeignConfig.class)
public interface DividendsClient {

    @GetMapping("/iss/securities/{secid}/dividends.xml?iss.meta=off")
    String getDividends(@PathVariable String secid);
}
