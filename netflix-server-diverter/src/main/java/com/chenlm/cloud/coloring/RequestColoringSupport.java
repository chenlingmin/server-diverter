package com.chenlm.cloud.coloring;

import com.chenlm.cloud.ServerDiverterProperties;
import com.chenlm.cloud.coloring.async.HttpHeadersHolder;
import com.chenlm.cloud.ribbon.support.RibbonFilterContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import java.util.Optional;

/**
 * @author Chenlm
 */
public abstract class RequestColoringSupport implements EnvironmentAware {

    private static final Logger logger = LoggerFactory.getLogger(RequestColoringSupport.class);

    @Autowired(required = false)
    private EurekaInstanceConfigBean eurekaInstanceConfigBean;

    private Environment environment;

    private final ServerDiverterProperties serverDiverterProperties;

    protected RequestColoringSupport(ServerDiverterProperties serverDiverterProperties) {
        this.serverDiverterProperties = serverDiverterProperties;
    }

    /**
     * 区分流量的请求头
     *
     * @return
     */
    public String headerName() {
        return serverDiverterProperties.getHttpHeaderName();
    }

    /**
     * 服务标记名称
     *
     * @return
     */
    public String serverMarkName() {
        return serverDiverterProperties.getServerMarkName();
    }

    /**
     * 获取当前流量标记，根据标记选择服务
     *
     * @return 流量标记
     */
    public String markServer() {
        RibbonFilterContextHolder.clearCurrentContext();
        String env = env();
        if (!StringUtils.isEmpty(env)) {
            RibbonFilterContextHolder.getCurrentContext().add(serverMarkName(), env);
        }
        return env;
    }

    private String env() {
        Optional<String> headerValue = HttpHeadersHolder.getHeaderValue(headerName());
        if (headerValue.isPresent()) {
            logger.debug("从 request 请求头 {} 中获取到 {} 标记的流量", headerName(), headerValue.get());
            return headerValue.get();
        }
        String serverMark = getServerMarkValue();
        if (StringUtils.isEmpty(serverMark)) {
            logger.debug("request 请求头 {} 中获取不到流量标记，也无法从 metadataMap.{} 配置中获取本机标签，将不能进行流量选择服务", headerName(), serverMarkName());
            return null;
        } else {
            logger.debug("request 请求头 {} 中获取不到流量标记，使用本机服务标签 {}", headerName(), serverMark);
            return serverMark;
        }
    }

    private String getServerMarkValue() {
        if (null != eurekaInstanceConfigBean) {
            return eurekaInstanceConfigBean.getMetadataMap().get(serverMarkName());
        }
        return environment.getProperty("eureka.instance.metadata-map." + serverMarkName());
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

}
