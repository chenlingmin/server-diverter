package com.chenlm.cloud.coloring.feign;

import feign.RequestInterceptor;
import com.chenlm.cloud.ServerDiverterProperties;
import com.chenlm.cloud.eureka.MarkServerConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(RequestInterceptor.class)
@AutoConfigureAfter(MarkServerConfiguration.class)
public class FeignColoringConfiguration {

    @Bean
    public RequestInterceptor requestInterceptor(ServerDiverterProperties serverDiverterProperties) {
        return new FeignColoringInterceptor(serverDiverterProperties);
    }
}
