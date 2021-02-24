package com.dakare.streamlabs.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "app.image-alert")
public class ImageAlertConfigurationProperties {

    private boolean enabled;
    private boolean loyaltyPoints;
}
