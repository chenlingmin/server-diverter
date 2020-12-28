package com.chenlm.cloud;

import com.chenlm.cloud.coloring.async.RequestInheritableAsyncConfigurerConfiguration;
import com.chenlm.cloud.coloring.async.RequestInheritableExecutorConfiguration;
import com.chenlm.cloud.coloring.feign.FeignColoringConfiguration;
import com.chenlm.cloud.coloring.hystrix.HystrixColoringConfiguration;
import com.chenlm.cloud.coloring.resttemplate.RestTemplateColoringConfiguration;
import com.chenlm.cloud.eureka.MarkServerConfiguration;
import com.chenlm.cloud.ribbon.support.RibbonDiscoveryRuleAutoConfiguration;
import com.chenlm.cloud.zuul.ServerDiverterZuulConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        RequestInheritableAsyncConfigurerConfiguration.class,
        RequestInheritableExecutorConfiguration.class,
        FeignColoringConfiguration.class,
        RestTemplateColoringConfiguration.class,
        HystrixColoringConfiguration.class,
        RibbonDiscoveryRuleAutoConfiguration.class,
        ServerDiverterZuulConfiguration.class,
        MarkServerConfiguration.class
})
@ConditionalOnProperty(prefix = ServerDiverterProperties.CONFIG_PREFIX, value = "enabled", matchIfMissing = true)
@EnableConfigurationProperties(ServerDiverterProperties.class)
public class ServerDiverterConfiguration {
}
