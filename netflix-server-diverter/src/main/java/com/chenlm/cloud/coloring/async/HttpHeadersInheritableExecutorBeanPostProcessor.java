package com.chenlm.cloud.coloring.async;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.util.concurrent.Executor;

/**
 * 处理 Spring Bean 中的 {@link Executor} 支持异步线程传递 HttpHeadersHolder 中的 httpHeaders
 *
 * @author Chenlm
 */
public class HttpHeadersInheritableExecutorBeanPostProcessor extends HttpHeaderInheritableExecutorProxySupport implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof Executor) {
            return createProxy((Executor) bean);
        }
        return bean;
    }

}

