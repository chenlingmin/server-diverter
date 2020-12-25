package com.chenlm.cloud.coloring.async;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.scheduling.annotation.AsyncConfigurer;

/**
 * @author Chenlm
 */
@Configuration
@ConditionalOnClass(AsyncConfigurer.class)
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
public class RequestInheritableAsyncConfigurerConfiguration {

    @Bean
    public RequestInheritableProxyAsyncConfigurerBeanPostProcessor requestInheritableAsyncConfigurerBeanPostProcessor() {
        return new RequestInheritableProxyAsyncConfigurerBeanPostProcessor();
    }


}
