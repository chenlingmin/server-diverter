package com.chenlm.cloud.server;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "server-b",
        fallback = FallbackServerBApi.class)

public interface ServerBApi {
    @GetMapping("test")
    String test();
}
