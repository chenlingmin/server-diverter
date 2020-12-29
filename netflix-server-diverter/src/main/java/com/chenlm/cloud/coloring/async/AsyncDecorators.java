package com.chenlm.cloud.coloring.async;

import com.chenlm.cloud.ribbon.support.RibbonFilterContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Chenlm
 */
public final class AsyncDecorators {

    private static HttpHeaders extractHttpHeaders(HttpServletRequest httpRequest) {
        return Collections.list((Enumeration<String>) httpRequest.getHeaderNames())
                .stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        h -> Collections.list((Enumeration<String>) httpRequest.getHeaders(h)),
                        (oldValue, newValue) -> newValue,
                        HttpHeaders::new
                ));
    }

    public static final Decorator<Runnable> HttpHeaderRunnableDecorator = runnable -> {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpHeaders httpHeaders;
        if (requestAttributes != null && requestAttributes instanceof ServletRequestAttributes) {
            httpHeaders = extractHttpHeaders(((ServletRequestAttributes) requestAttributes).getRequest());
        } else {
            httpHeaders = HttpHeadersHolder.getHttpHeaders();
        }

        if (httpHeaders != null) {
            return () -> {
                try {
                    HttpHeadersHolder.setHttpHeader(httpHeaders);
                    runnable.run();
                } finally {
                    HttpHeadersHolder.resetHttpHeader();
                }
            };
        } else {
            return runnable;
        }
    };


    public static final Decorator<Callable> HttpHeaderCallableDecorator = callable -> {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpHeaders httpHeaders;
        if (requestAttributes != null && requestAttributes instanceof ServletRequestAttributes) {
            httpHeaders = extractHttpHeaders(((ServletRequestAttributes) requestAttributes).getRequest());
        } else {
            httpHeaders = HttpHeadersHolder.getHttpHeaders();
        }

        if (httpHeaders != null) {
            return () -> {
                try {
                    HttpHeadersHolder.setHttpHeader(httpHeaders);
                    return callable.call();
                } finally {
                    HttpHeadersHolder.resetHttpHeader();
                }
            };
        } else {
            return callable;
        }
    };

    public static final Decorator<Callable> RibbonFilterContextCallableDecorator = callable -> {
        Map<String, String> attributes = new HashMap<>(RibbonFilterContextHolder.getCurrentContext().getAttributes());
        if (!attributes.isEmpty()) {
            return () -> {
                try {
                    RibbonFilterContextHolder.clearCurrentContext();
                    attributes.forEach((k, v) -> RibbonFilterContextHolder.getCurrentContext().add(k, v));
                    return callable.call();
                } finally {
                    RibbonFilterContextHolder.clearCurrentContext();
                }
            };
        } else {
            return callable;
        }
    };
}
