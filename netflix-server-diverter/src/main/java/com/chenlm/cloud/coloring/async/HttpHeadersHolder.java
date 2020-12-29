package com.chenlm.cloud.coloring.async;

import org.springframework.core.NamedThreadLocal;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Chenlm
 */
public final class HttpHeadersHolder {

    private static final ThreadLocal<HttpHeaders> httpHeadersThreadLocal =
            new NamedThreadLocal<>("Http Headers");


    public static void resetHttpHeader() {
        httpHeadersThreadLocal.remove();
    }

    public static void setHttpHeader(HttpHeaders httpHeaders) {
        if (httpHeaders == null) {
            resetHttpHeader();
        } else {
            HttpHeadersHolder.httpHeadersThreadLocal.set(httpHeaders);
        }
    }

    /**
     * Return the RequestAttributes currently bound to the thread.
     *
     * @return the RequestAttributes currently bound to the thread,
     * or {@code null} if none bound
     */
    public static HttpHeaders getHttpHeaders() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null && requestAttributes instanceof ServletRequestAttributes) {
            HttpServletRequest httpRequest = ((ServletRequestAttributes) requestAttributes).getRequest();
            return Collections.list((Enumeration<String>) httpRequest.getHeaderNames())
                    .stream()
                    .collect(Collectors.toMap(
                            Function.identity(),
                            h -> Collections.list((Enumeration<String>) httpRequest.getHeaders(h)),
                            (oldValue, newValue) -> newValue,
                            HttpHeaders::new
                    ));
        }
        return httpHeadersThreadLocal.get();
    }

    public static Optional<String> getHeaderValue(String headerName) {
        return Optional.ofNullable(getHttpHeaders())
                .map(httpHeaders -> httpHeaders.getFirst(headerName));
    }

    public static List<String> getHeaderValues(String headerName) {
        return Optional.ofNullable(getHttpHeaders())
                .map(httpHeaders -> httpHeaders.get(headerName))
                .orElseGet(Collections::emptyList);
    }


}
