package com.dakare.streamlabs.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.concurrent.TimeUnit;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "app.stream-info")
public class StreamInfoConfigurationProperties {

  private boolean enabled = true;
  private long refreshIntervalMs = TimeUnit.SECONDS.toMillis(30);
  private long onlineStatusProlongSec = TimeUnit.MINUTES.toSeconds(5);
  private String clientId = "";
}
