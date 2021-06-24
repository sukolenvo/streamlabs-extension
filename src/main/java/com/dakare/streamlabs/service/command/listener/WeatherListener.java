package com.dakare.streamlabs.service.command.listener;

import com.dakare.streamlabs.config.properties.WeatherConfigurationProperties;
import com.dakare.streamlabs.service.command.ChatCommand;
import com.dakare.streamlabs.service.command.CommandSender;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherListener {

  @Autowired
  private WeatherConfigurationProperties configurationProperties;

  @Autowired
  private RestTemplate restTemplate;
  @Autowired
  private CommandSender commandSender;
  private long nextAlert = 0;

  @EventListener
  public void onCommand(ChatCommand chatCommand) {
    if (!configurationProperties.isEnabled()) {
      return;
    }
    if (!chatCommand.getCommand().equalsIgnoreCase("погода")) {
      return;
    }
    synchronized (WeatherListener.class) {
      if (nextAlert > System.currentTimeMillis()) {
        log.info("Skip command {} - too early", chatCommand);
        return;
      }
      String request = chatCommand.getArgs();
      if (!request.matches("^[\\p{L}\\p{L}\\d ,]+$")) {
        log.info("Invalid request string: {}", request);
        return;
      }
      ResponseEntity<WeatherResponse> response = restTemplate
          .getForEntity(configurationProperties.getUrl() + "&q=" + request, WeatherResponse.class);
      if (response.getStatusCode() == HttpStatus.OK) {
        WeatherResponse result = response.getBody();
        String city = result.getName();
        String county = result.getSys().getCountry();
        double temp = result.getMain().getTemp();
        int humidity = result.getMain().getHumidity();
        double wind = result.getWind().getSpeed();
        String description = result.getWeather().stream().findAny().map(Weather::getDescription).orElse(null);
        if (StringUtils.isNoneEmpty(city, county, description)) {
          String message = String
              .format("%s(%s): %+.0f°C, влажность %d%%, ветер %.2fм/c, %s", city, county, temp, humidity, wind, description);
          commandSender.sendMessage(message);
        }
      }
      nextAlert = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(configurationProperties.getDelaySec());
    }
  }

  @Data
  private static class WeatherResponse {
    private String name;
    private Sys sys;
    private Wind wind;
    private Main main;
    private List<Weather> weather;
  }

  @Data
  private static class Sys {
    private String country;
  }

  @Data
  private static class Wind {
    private double speed;
  }

  @Data
  private static class Main {
    private double temp;
    private int humidity;
  }

  @Data
  private static class Weather {
    private String description;
  }
}
