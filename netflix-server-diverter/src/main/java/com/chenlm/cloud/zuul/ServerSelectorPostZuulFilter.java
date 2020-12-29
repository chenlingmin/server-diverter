package com.chenlm.cloud.zuul;

import com.netflix.zuul.ZuulFilter;
import com.chenlm.cloud.ribbon.support.RibbonFilterContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;

/**
 * @author Chenlm
 */
public class ServerSelectorPostZuulFilter extends ZuulFilter {
    private static final Logger logger = LoggerFactory.getLogger(ServerSelectorPostZuulFilter.class);

    public ServerSelectorPostZuulFilter() {
    }

    @Override
    public String filterType() {
        return FilterConstants.POST_TYPE;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RibbonFilterContextHolder.clearCurrentContext();
        logger.debug("清理 RibbonFilterContextHolder");
        return null;
    }
}

