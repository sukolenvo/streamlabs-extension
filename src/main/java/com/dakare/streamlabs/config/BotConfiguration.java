package com.dakare.streamlabs.config;

import com.dakare.streamlabs.config.properties.BotConfigurationProperties;
import com.dakare.streamlabs.config.properties.ImageAlertConfigurationProperties;
import com.dakare.streamlabs.config.properties.StreamInfoConfigurationProperties;
import com.dakare.streamlabs.config.properties.WeatherConfigurationProperties;
import com.dakare.streamlabs.repository.AlertUsageRepository;
import com.dakare.streamlabs.repository.ImageAlertRepository;
import com.dakare.streamlabs.service.PircBotManager;
import com.dakare.streamlabs.service.StreamStatusService;
import com.dakare.streamlabs.service.StreamlabsAlertService;
import com.dakare.streamlabs.service.StreamlabsPointsService;
import com.dakare.streamlabs.service.command.CommandListenerAdapter;
import com.dakare.streamlabs.service.command.CommandSender;
import com.dakare.streamlabs.service.command.CommandSenderImpl;
import com.dakare.streamlabs.service.command.listener.ImageAlertListener;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.pircbotx.PircBotX;
import org.pircbotx.cap.EnableCapHandler;
import org.pircbotx.hooks.Listener;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Configuration
@EnableConfigurationProperties({BotConfigurationProperties.class, StreamInfoConfigurationProperties.class,
    ImageAlertConfigurationProperties.class,
    WeatherConfigurationProperties.class})
public class BotConfiguration {

  @Bean
  public PircBotX getBot(BotConfigurationProperties config, List<Listener> adapters) {
    Preconditions.checkArgument(StringUtils.isNotEmpty(config.getChannel()),
        "twitch bot channel configuration is missing in application.properties");
    Preconditions.checkArgument(StringUtils.isNotEmpty(config.getName()),
        "twitch bot name configuration is missing in application.properties");
    Preconditions.checkArgument(StringUtils.isNotEmpty(config.getServer()),
        "twitch bot server host configuration is missing in application.properties");
    Preconditions.checkArgument(config.getPort() != 0,
        "twitch bot server port configuration is missing in application.properties");
    Preconditions.checkArgument(StringUtils.isNotEmpty(config.getOauth()),
        "twitch bot oauth token is missing in application.properties");
    org.pircbotx.Configuration botConfiguration = new org.pircbotx.Configuration.Builder()
        .setName(config.getName())
        .addServer(config.getServer(), config.getPort())
        .setServerPassword(config.getOauth())
        .addListeners(adapters)
        .addAutoJoinChannel("#" + config.getChannel())
        .setAutoReconnect(true)
        .addCapHandler(new EnableCapHandler("twitch.tv/tags"))
        .addCapHandler(new EnableCapHandler("twitch.tv/commands"))
        .buildConfiguration();
    return new PircBotX(botConfiguration);
  }

  @Bean
  public PircBotManager pircBotManager(PircBotX bot) {
    return new PircBotManager(bot);
  }

  @Bean
  public CommandListenerAdapter commandListenerAdapter(ApplicationEventPublisher publisher) {
    return new CommandListenerAdapter(publisher);
  }

  @Bean
  public CommandSender commandSender(PircBotX pircBotX, BotConfigurationProperties botConfigurationProperties) {
    return new CommandSenderImpl(pircBotX, botConfigurationProperties);
  }

  @Bean
  public StreamStatusService streamStatusService(RestTemplate restTemplate,
      TaskScheduler taskScheduler,
      StreamInfoConfigurationProperties streamInfoConfigurationProperties,
      BotConfigurationProperties botConfigurationProperties) {
    return new StreamStatusService(restTemplate, taskScheduler,
        streamInfoConfigurationProperties, botConfigurationProperties);
  }

  @Bean
  public ImageAlertListener imageAlertListener(ImageAlertConfigurationProperties configurationProperties,
      ImageAlertRepository imageAlertRepository,
      StreamlabsAlertService streamlabsAlertService,
      AlertUsageRepository alertUsageRepository,
      StreamlabsPointsService streamlabsPointsService) {
    return new ImageAlertListener(streamlabsPointsService, configurationProperties,
        imageAlertRepository, streamlabsAlertService, alertUsageRepository);
  }

}
