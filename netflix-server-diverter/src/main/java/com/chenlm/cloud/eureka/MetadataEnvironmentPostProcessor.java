package com.chenlm.cloud.eureka;

import com.chenlm.cloud.ServerDiverterProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.boot.context.config.ConfigFileApplicationListener.ACTIVE_PROFILES_PROPERTY;

@Deprecated
public class MetadataEnvironmentPostProcessor implements EnvironmentPostProcessor {
    private static final Logger logger = LoggerFactory.getLogger(MetadataEnvironmentPostProcessor.class);

    private final ServerDiverterProperties serverDiverterProperties;

    private static final String DEFAULT_PROPERTIES = "defaultProperties";

    private static final String EUREKA_METADATA_KEY = "eureka.instance.metadata-map.";

    public MetadataEnvironmentPostProcessor(ServerDiverterProperties serverDiverterProperties) {
        this.serverDiverterProperties = serverDiverterProperties;
    }

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        if (environment.containsProperty(EUREKA_METADATA_KEY + serverDiverterProperties.getServerMarkName())) {
            return;
        }
        if (!environment.containsProperty(ACTIVE_PROFILES_PROPERTY)) {
            return;
        }
        MutablePropertySources propertySources = environment.getPropertySources();

        MapPropertySource target = null;
        if (propertySources.contains(DEFAULT_PROPERTIES)) {
            PropertySource<?> source = propertySources.get(DEFAULT_PROPERTIES);
            if (source instanceof MapPropertySource) {
                target = (MapPropertySource) source;
                target.getSource().put(EUREKA_METADATA_KEY + serverDiverterProperties.getServerMarkName(), environment.getProperty(ACTIVE_PROFILES_PROPERTY));
            }
            if (target == null) {

                Map<String, Object> map = new HashMap<>();
                map.put(EUREKA_METADATA_KEY + serverDiverterProperties.getServerMarkName(), environment.getProperty(ACTIVE_PROFILES_PROPERTY));
                target = new MapPropertySource(DEFAULT_PROPERTIES, map);
            }
            if (!propertySources.contains(DEFAULT_PROPERTIES)) {
                propertySources.addLast(target);
            }
        }
    }
}
