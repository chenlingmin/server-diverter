package com.chenlm.cloud.eureka;

import com.chenlm.cloud.ServerDiverterProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.env.Environment;

import java.util.Map;

import static org.springframework.boot.context.config.ConfigFileApplicationListener.ACTIVE_PROFILES_PROPERTY;

/**
 * 去 Eureka 标记服务的标签，标记到 metadataMap 中
 * 标记内容为 {@link ServerDiverterProperties#getServerMarkName()} = ${spring.profiles.active}
 *
 * @author Chenlm
 */
public class MarkServerPostProcessor implements BeanPostProcessor, EnvironmentAware, PriorityOrdered {
    private static final Logger logger = LoggerFactory.getLogger(MarkServerPostProcessor.class);

    private Environment environment;
    private final ServerDiverterProperties serverDiverterProperties;

    public MarkServerPostProcessor(ServerDiverterProperties serverDiverterProperties) {
        this.serverDiverterProperties = serverDiverterProperties;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof EurekaInstanceConfigBean) {
            Map<String, String> metadataMap = ((EurekaInstanceConfigBean) bean).getMetadataMap();
            if (!metadataMap.containsKey(serverDiverterProperties.getServerMarkName()) && environment.containsProperty(ACTIVE_PROFILES_PROPERTY)) {
                metadataMap.put(serverDiverterProperties.getServerMarkName(), environment.getProperty(ACTIVE_PROFILES_PROPERTY));
                logger.info("服务使用默认标记，{} = {}", serverDiverterProperties.getServerMarkName(), environment.getProperty(ACTIVE_PROFILES_PROPERTY));
            }
        }
        return bean;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE;
    }
}