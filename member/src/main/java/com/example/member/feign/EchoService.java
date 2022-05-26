package com.example.member.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("mall-coupon")
public interface EchoService {
    @GetMapping(value = "/echo/{string}")
    public String echo(@PathVariable String string);
}
