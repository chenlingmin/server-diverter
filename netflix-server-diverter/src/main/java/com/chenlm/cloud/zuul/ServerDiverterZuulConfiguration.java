package com.chenlm.cloud.zuul;

import com.chenlm.cloud.ServerDiverterProperties;
import com.netflix.zuul.ZuulFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@ConditionalOnClass(ZuulFilter.class)
@Configuration
public class ServerDiverterZuulConfiguration {


    @Bean
    @ConditionalOnMissingBean
    public ServerSelector simpleHeaderServerSelector(ServerDiverterProperties properties) {
        return new SimpleHeaderServerSelector(properties.getHttpHeaderName());
    }


    @Bean
    public ZuulFilter testZuulFilter(ServerDiverterProperties properties, List<ServerSelector> serverSelectors) {
        return new ServerDiverterZuulFilter(properties, serverSelectors);
    }
}
