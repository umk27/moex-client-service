package com.bondservice.moexclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "couponClient", url = "https://iss.moex.com",
        configuration = FeignConfig.class)
public interface CouponClient {

    @GetMapping("/iss/securities/{secid}/bondization.xml?iss.meta=off&iss.only=coupons&start=0&limit=100")
    String getCoupons(@PathVariable("secid") String secid);
}
