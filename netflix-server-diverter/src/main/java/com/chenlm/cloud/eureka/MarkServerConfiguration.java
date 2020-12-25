package com.chenlm.cloud.eureka;

import com.chenlm.cloud.ServerDiverterProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(EurekaInstanceConfigBean.class)
public class MarkServerConfiguration {

    @Bean
    public MarkServerPostProcessor metadataPostProcessor(ServerDiverterProperties serverDiverterProperties) {
        return new MarkServerPostProcessor(serverDiverterProperties);
    }

}
