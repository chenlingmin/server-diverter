package com.chenlm.cloud.server;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.web.servlet.FrameworkServlet;

//@Component
public class InheritableServletBeanProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof FrameworkServlet) {
            ((FrameworkServlet) bean).setThreadContextInheritable(true);
        }
        return bean;
    }
}
