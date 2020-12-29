package com.chenlm.cloud.zuul;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.chenlm.cloud.ServerDiverterProperties;
import com.chenlm.cloud.ribbon.support.RibbonFilterContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Chenlm
 */
public class ServerSelectorPreZuulFilter extends ZuulFilter {
    private static final Logger logger = LoggerFactory.getLogger(ServerSelectorPreZuulFilter.class);

    private final ServerDiverterProperties properties;
    private final List<ServerSelector> serverSelectors;

    public ServerSelectorPreZuulFilter(ServerDiverterProperties properties, List<ServerSelector> serverSelectors) {
        this.properties = properties;
        this.serverSelectors = serverSelectors;
    }

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
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
        HttpServletRequest request = RequestContext.getCurrentContext()
                .getRequest();
        String env = null;
        for (ServerSelector serverSelector : serverSelectors) {
            if (serverSelector.support(request)) {
                env = serverSelector.select(request);
                logger.debug("{} 选择出 {}", serverSelector, env);
                break;
            }
        }
        if (StringUtils.isEmpty(env)) {
            logger.debug("不使用标签选择服务");
            return null;
        }
        logger.debug("准备选择标签为 {} 的服务", env);
        RibbonFilterContextHolder.getCurrentContext().add(properties.getServerMarkName(), env);
        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.addZuulRequestHeader(properties.getHttpHeaderName(), env);
        return null;
    }
}

