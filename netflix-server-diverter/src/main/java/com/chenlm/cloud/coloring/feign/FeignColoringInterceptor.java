package com.chenlm.cloud.coloring.feign;


import feign.RequestInterceptor;
import feign.RequestTemplate;
import com.chenlm.cloud.ServerDiverterProperties;
import com.chenlm.cloud.coloring.RequestColoringSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * 处理  Feign 染色流量，与选择服务
 *
 * @author Chenlm
 */
public class FeignColoringInterceptor extends RequestColoringSupport implements RequestInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(FeignColoringInterceptor.class);

    public FeignColoringInterceptor(ServerDiverterProperties serverDiverterProperties) {
        super(serverDiverterProperties);
    }

    @Override
    public void apply(RequestTemplate template) {
        String env = markServer();
        if (!StringUtils.isEmpty(env)) {
            addHeader(template, headerName(), env);
        }
    }

    void addHeader(RequestTemplate requestTemplate, String name, String values) {
        if (!requestTemplate.headers().containsKey(name)) {
            logger.debug("在 Feign 中添加请求头 {} = {}", name, values);
            requestTemplate.header(name, values);
        }
    }

}
