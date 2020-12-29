package com.chenlm.cloud.coloring.resttemplate;

import com.chenlm.cloud.ServerDiverterProperties;
import com.chenlm.cloud.coloring.async.HttpHeaderInheritableExecutorProxySupport;
import com.chenlm.cloud.coloring.resttemplate.RestTemplateColoringBeanProcessor.AsyncRestTemplateColoringInterceptor;
import com.chenlm.cloud.coloring.resttemplate.RestTemplateColoringBeanProcessor.RestTemplateColoringInterceptor;
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
public class RestTemplateColoringConfiguration extends HttpHeaderInheritableExecutorProxySupport {


    @Bean
    public RestTemplateColoringInterceptor restTemplateColoringInterceptor(ServerDiverterProperties serverDiverterProperties) {
        return new RestTemplateColoringInterceptor(serverDiverterProperties);
    }

    @Bean
    public AsyncRestTemplateColoringInterceptor asyncRestTemplateColoringInterceptor(ServerDiverterProperties serverDiverterProperties) {
        return new AsyncRestTemplateColoringInterceptor(serverDiverterProperties);
    }

    @Bean
    public RestTemplateColoringBeanProcessor restTemplateColoringBeanProcessor(
            RestTemplateColoringInterceptor restTemplateColoringInterceptor,
            AsyncRestTemplateColoringInterceptor asyncRestTemplateColoringInterceptor
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
