package com.dakare.streamlabs.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "app.streamlabs-alert")
public class StreamlabsAlertConfigurationProperties {

    private Type type;
    private String token;
    private String defaultSoundUrl;
    private int defaultDurationMs;
    private int defaultDelaySec;
    private String defaultMessage;
    private String defaultUserMessage;
    private String defaultColor;
    private int defaultPrice;
    private int defaultSubscriberPrice;

    public enum Type {
        FOLLOW, SUBSCRIPTION, DONATION, HOST
    }
}
