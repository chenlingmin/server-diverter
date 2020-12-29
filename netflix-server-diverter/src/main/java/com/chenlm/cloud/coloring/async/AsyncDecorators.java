package com.chenlm.cloud.coloring.async;

import com.chenlm.cloud.ribbon.support.RibbonFilterContextHolder;
import org.springframework.http.HttpHeaders;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import static com.chenlm.cloud.coloring.async.HttpHeadersHolder.getHttpHeaders;

/**
 * @author Chenlm
 */
public final class AsyncDecorators {


    public static final Decorator<Runnable> HttpHeaderRunnableDecorator = runnable -> {
        HttpHeaders httpHeaders = getHttpHeaders();
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
        HttpHeaders httpHeaders = getHttpHeaders();
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
