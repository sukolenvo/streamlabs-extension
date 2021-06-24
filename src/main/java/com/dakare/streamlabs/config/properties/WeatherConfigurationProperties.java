package com.dakare.streamlabs.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "app.weather")
public class WeatherConfigurationProperties {

  private boolean enabled;
  private String url;
  private int delaySec = 180;
}
