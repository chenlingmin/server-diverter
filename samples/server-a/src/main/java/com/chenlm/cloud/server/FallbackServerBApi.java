package com.chenlm.cloud.server;

import org.springframework.stereotype.Component;

@Component
public class FallbackServerBApi implements ServerBApi {
    @Override
    public String test() {
        return "fallback";
    }
}