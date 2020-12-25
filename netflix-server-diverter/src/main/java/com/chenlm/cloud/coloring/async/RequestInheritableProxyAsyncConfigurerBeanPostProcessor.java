package com.chenlm.cloud.coloring.async;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.PriorityOrdered;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;

import java.util.concurrent.Executor;

/**
 * 处理 Spring 异步配置 {@link AsyncConfigurer}支持异步线程传递 RequestHolder 中的 requestAttributes
 */

public class RequestInheritableProxyAsyncConfigurerBeanPostProcessor extends ExecutorRequestInheritableProxySupport implements BeanPostProcessor, PriorityOrdered {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof AsyncConfigurer) {
            return new AsyncConfigurerSupport() {
                @Override
                public Executor getAsyncExecutor() {
                    Executor asyncExecutor = ((AsyncConfigurer) bean).getAsyncExecutor();
                    return createProxy(asyncExecutor);
                }

                @Override
                public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
                    return ((AsyncConfigurer) bean).getAsyncUncaughtExceptionHandler();
                }
            };
        }
        return bean;
    }

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE;
    }
}
