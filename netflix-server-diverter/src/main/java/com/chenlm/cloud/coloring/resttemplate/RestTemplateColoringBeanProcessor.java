package com.chenlm.cloud.coloring.resttemplate;

import com.chenlm.cloud.ServerDiverterProperties;
import com.chenlm.cloud.coloring.RequestColoringSupport;
import com.chenlm.cloud.coloring.async.HttpHeaderInheritableExecutorProxySupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.*;
import org.springframework.util.StringUtils;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

/**
 * @author Chenlm
 */
public class RestTemplateColoringBeanProcessor extends HttpHeaderInheritableExecutorProxySupport implements BeanPostProcessor {
    private static final Logger logger = LoggerFactory.getLogger(RestTemplateColoringBeanProcessor.class);

    private final RestTemplateColoringInterceptor restTemplateColoringInterceptor;
    private final AsyncRestTemplateColoringInterceptor asyncRestTemplateColoringInterceptor;

    public RestTemplateColoringBeanProcessor(RestTemplateColoringInterceptor restTemplateColoringInterceptor, AsyncRestTemplateColoringInterceptor asyncRestTemplateColoringInterceptor) {
        this.restTemplateColoringInterceptor = restTemplateColoringInterceptor;
        this.asyncRestTemplateColoringInterceptor = asyncRestTemplateColoringInterceptor;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof RestTemplate) {
            RestTemplate restTemplate = (RestTemplate) bean;
            List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();
            if (interceptors.contains(restTemplateColoringInterceptor)) {
                return restTemplate;
            }
            interceptors.add(restTemplateColoringInterceptor);
            restTemplate.setInterceptors(interceptors);
            logger.info("配置 RestTemplate 支持流量染色，与服务选择");
            return restTemplate;
        } else if (bean instanceof AsyncRestTemplate) {
            AsyncRestTemplate asyncRestTemplate = (AsyncRestTemplate) bean;
            List<AsyncClientHttpRequestInterceptor> interceptors = asyncRestTemplate.getInterceptors();
            if (interceptors.contains(asyncRestTemplateColoringInterceptor)) {
                return asyncRestTemplate;
            }
            interceptors.add(asyncRestTemplateColoringInterceptor);
            asyncRestTemplate.setInterceptors(interceptors);
            logger.info("配置 AsyncRestTemplate 支持流量染色，与服务选择");
            return asyncRestTemplate;
        }
        return bean;
    }

    public static class RestTemplateColoringInterceptor extends RequestColoringSupport implements ClientHttpRequestInterceptor {

        private static final Logger logger = LoggerFactory.getLogger(RestTemplateColoringInterceptor.class);

        public RestTemplateColoringInterceptor(ServerDiverterProperties serverDiverterProperties) {
            super(serverDiverterProperties);
        }

        @Override
        public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
            String env = markServer();
            if (!StringUtils.isEmpty(env)) {
                HttpHeaders headers = request.getHeaders();
                headers.add(headerName(), env);
                logger.debug("在 RestTemplate 中添加请求头 {} = {}", headerName(), env);
            }
            return execution.execute(request, body);
        }
    }

    public static class AsyncRestTemplateColoringInterceptor extends RequestColoringSupport implements AsyncClientHttpRequestInterceptor {
        private static final Logger logger = LoggerFactory.getLogger(AsyncRestTemplateColoringInterceptor.class);

        protected AsyncRestTemplateColoringInterceptor(ServerDiverterProperties serverDiverterProperties) {
            super(serverDiverterProperties);
        }

        @Override
        public ListenableFuture<ClientHttpResponse> intercept(HttpRequest request, byte[] body, AsyncClientHttpRequestExecution execution) throws IOException {
            String env = markServer();
            if (!StringUtils.isEmpty(env)) {
                HttpHeaders headers = request.getHeaders();
                headers.add(headerName(), env);
                logger.debug("在 AsyncRestTemplate 中添加请求头 {} = {}", headerName(), env);
            }
            return execution.executeAsync(request, body);
        }

    }
}
