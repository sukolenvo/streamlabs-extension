package com.dakare.streamlabs.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "app.bot")
public class BotConfigurationProperties {

  private String name;
  private String server;
  private int port;
  private String oauth;
  private String channel;
}
