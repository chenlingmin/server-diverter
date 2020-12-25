package com.chenlm.cloud;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = ServerDiverterProperties.CONFIG_PREFIX)
public class ServerDiverterProperties {
    /**
     * 流量染色的http请求头
     */
    private String httpHeaderName = "x-env";
    /**
     * 服务注册的 metadataMap 的 key
     */
    private String serverMarkName = "env";
    private boolean enabled = true;

    public final static String CONFIG_PREFIX = "server.diverter";

    public String getHttpHeaderName() {
        return httpHeaderName;
    }

    public void setHttpHeaderName(String httpHeaderName) {
        this.httpHeaderName = httpHeaderName;
    }

    public String getServerMarkName() {
        return serverMarkName;
    }

    public void setServerMarkName(String serverMarkName) {
        this.serverMarkName = serverMarkName;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "TestProperties{" +
                "httpHeaderName='" + httpHeaderName + '\'' +
                ", serverMarkName='" + serverMarkName + '\'' +
                ", enabled=" + enabled +
                '}';
    }
}
