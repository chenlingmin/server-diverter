package com.chenlm.cloud.coloring.feign;

import com.chenlm.cloud.ServerDiverterProperties;
import feign.RequestInterceptor;
import com.chenlm.cloud.eureka.MarkServerConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(RequestInterceptor.class)
@AutoConfigureAfter(MarkServerConfiguration.class)
public class FeignConfiguration {

    @Bean
    public RequestInterceptor requestInterceptor(ServerDiverterProperties serverDiverterProperties) {
        return new FeignColoringInterceptor(serverDiverterProperties);
    }
}
