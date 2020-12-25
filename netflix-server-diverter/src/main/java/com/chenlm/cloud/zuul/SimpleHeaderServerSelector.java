package com.chenlm.cloud.zuul;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

public class SimpleHeaderServerSelector implements ServerSelector{
    private static final Logger logger = LoggerFactory.getLogger(SimpleHeaderServerSelector.class);

    private final String headerName;

    public SimpleHeaderServerSelector(String headerName) {
        this.headerName = headerName;
    }

    @Override
    public boolean support(HttpServletRequest request) {
        return request.getHeader(headerName) != null;
    }

    @Override
    public String select(HttpServletRequest request) {
        return request.getHeader(headerName);
    }

    @Override
    public String toString() {
        return "SimpleHeaderServerSelector{" +
                "headerName='" + headerName + '\'' +
                '}';
    }
}
