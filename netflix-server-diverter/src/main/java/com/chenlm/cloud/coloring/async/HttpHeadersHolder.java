package com.chenlm.cloud.coloring.async;

import org.springframework.core.NamedThreadLocal;
import org.springframework.http.HttpHeaders;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author Chenlm
 */
public final class HttpHeadersHolder {

    private static final ThreadLocal<HttpHeaders> requestAttributesHolder =
            new NamedThreadLocal<>("Http Headers");


    public static void resetHttpHeader() {
        requestAttributesHolder.remove();
    }

    public static void setHttpHeader(HttpHeaders httpHeaders) {
        if (httpHeaders == null) {
            resetHttpHeader();
        } else {
            requestAttributesHolder.set(httpHeaders);
        }
    }

    /**
     * Return the RequestAttributes currently bound to the thread.
     *
     * @return the RequestAttributes currently bound to the thread,
     * or {@code null} if none bound
     */
    public static HttpHeaders getHttpHeaders() {
        return requestAttributesHolder.get();
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
