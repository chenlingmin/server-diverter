package com.chenlm.cloud.zuul;

import javax.servlet.http.HttpServletRequest;


/**
 * 服务选择器
 * 用户可以自定义多个服务选择器，应该注意选择器之间的顺序
 *
 * @author Chenlm
 */
public interface ServerSelector {

    /**
     * 表示该选择器是否支持此类请求
     *
     * @param request
     * @return
     */
    boolean support(HttpServletRequest request);

    /**
     * 选择标签服务
     *
     * @param request
     * @return
     */
    String select(HttpServletRequest request);
}
