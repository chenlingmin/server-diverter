package com.chenlm.cloud.coloring.async;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncListenableTaskExecutor;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

/**
 * @author Chenlm
 */
@Configuration
public class HttpHeadersInheritableExecutorConfiguration {
    @Bean
    public HttpHeadersInheritableExecutorBeanPostProcessor requestHolderInheritableExecutorBeanPostProcessor() {
        return new HttpHeadersInheritableExecutorBeanPostProcessor();
    }

    @Bean
    @ConditionalOnMissingBean
    public AsyncListenableTaskExecutor asyncListenableTaskExecutor() {
        return new SimpleAsyncTaskExecutor();
    }
}
