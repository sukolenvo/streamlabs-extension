package com.dakare.streamlabs.service;

import com.dakare.streamlabs.config.properties.BotConfigurationProperties;
import com.dakare.streamlabs.config.properties.StreamInfoConfigurationProperties;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
public class StreamStatusService implements Runnable {

  private static final String TEMPATE = "https://api.twitch.tv/kraken/streams/{channel}?client_id={clientId}";

  private final RestTemplate template;
  private final TaskScheduler taskScheduler;
  private final StreamInfoConfigurationProperties properties;
  private final BotConfigurationProperties botConfigurationProperties;
  private long lastOnline;

  @PostConstruct
  public void init() {
    if (properties.isEnabled()) {
      Preconditions.checkArgument(StringUtils.isNotEmpty(properties.getClientId()), "clientId is required");
      taskScheduler.scheduleWithFixedDelay(this, properties.getRefreshIntervalMs());
    }
  }

  @Override
  public void run() {
    ResponseEntity<Response> response = template
        .getForEntity(TEMPATE, Response.class, ImmutableMap.of("channel", botConfigurationProperties.getChannel(),
                                                               "clientId", properties.getClientId()));
    if (response.getStatusCode() == HttpStatus.OK) {
      if (response.getBody() != null
          && response.getBody().getStream() != null
          && "live".equals(response.getBody().getStream().getStreamType())) {
        lastOnline = System.currentTimeMillis();
      }
    } else {
      log.warn("Non-success response code {}", response.getStatusCode());
    }
  }

  public boolean isStreamOnline() {
    return lastOnline + TimeUnit.SECONDS.toMillis(properties.getOnlineStatusProlongSec()) > System.currentTimeMillis();
  }

  @Data
  @JsonIgnoreProperties(ignoreUnknown = true)
  private static class Response {
    private StreamInfo stream;
  }

  @Data
  @JsonIgnoreProperties(ignoreUnknown = true)
  private static class StreamInfo {
    @JsonProperty("stream_type")
    private String streamType;
  }
}
