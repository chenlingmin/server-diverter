package com.chenlm.cloud.coloring.resttemplate;

import com.chenlm.cloud.ServerDiverterProperties;
import com.chenlm.cloud.coloring.async.ExecutorRequestInheritableProxySupport;
import com.chenlm.cloud.eureka.MarkServerConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

/**
 * @author Chenlm
 */
@Configuration
@ConditionalOnClass({RestTemplate.class, AsyncRestTemplate.class})
@AutoConfigureAfter(MarkServerConfiguration.class)
public class RestTemplateConfiguration extends ExecutorRequestInheritableProxySupport {


    @Bean
    public RestTemplateColoringBeanProcessor.RestTemplateColoringInterceptor restTemplateColoringInterceptor(ServerDiverterProperties serverDiverterProperties) {
        return new RestTemplateColoringBeanProcessor.RestTemplateColoringInterceptor(serverDiverterProperties);
    }

    @Bean
    public RestTemplateColoringBeanProcessor.AsyncRestTemplateColoringInterceptor asyncRestTemplateColoringInterceptor(ServerDiverterProperties serverDiverterProperties) {
        return new RestTemplateColoringBeanProcessor.AsyncRestTemplateColoringInterceptor(serverDiverterProperties);
    }

    @Bean
    public RestTemplateColoringBeanProcessor restTemplateColoringBeanProcessor(
            RestTemplateColoringBeanProcessor.RestTemplateColoringInterceptor restTemplateColoringInterceptor,
            RestTemplateColoringBeanProcessor.AsyncRestTemplateColoringInterceptor asyncRestTemplateColoringInterceptor
    ) {
        return new RestTemplateColoringBeanProcessor(
                restTemplateColoringInterceptor,
                asyncRestTemplateColoringInterceptor
        );
    }

    @Bean
    @ConditionalOnMissingBean
    public AsyncRestTemplate traceAsyncRestTemplate() {
        SimpleAsyncTaskExecutor simpleAsyncTaskExecutor = createProxy(
                new SimpleAsyncTaskExecutor("AsyncRestTemplate")
        );
        return new AsyncRestTemplate(simpleAsyncTaskExecutor);
    }

}
