package com.dakare.streamlabs.config;

import com.dakare.streamlabs.config.properties.BotConfigurationProperties;
import com.dakare.streamlabs.config.properties.StreamlabsAlertConfigurationProperties;
import com.dakare.streamlabs.config.properties.StreamlabsOauthConfigurationProperties;
import com.dakare.streamlabs.repository.AppDbPropertyRepository;
import com.dakare.streamlabs.service.StreamlabsAlertService;
import com.dakare.streamlabs.service.StreamlabsOauthService;
import com.dakare.streamlabs.service.StreamlabsPointsService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableConfigurationProperties({StreamlabsAlertConfigurationProperties.class, StreamlabsOauthConfigurationProperties.class})
public class SteamlabsConfiguration {

  @Bean
  public StreamlabsOauthService streamlabsOauthService(
          StreamlabsOauthConfigurationProperties streamlabsOauthConfigurationProperties,
          AppDbPropertyRepository appDbPropertyRepository) {
    return new StreamlabsOauthService(streamlabsOauthConfigurationProperties, appDbPropertyRepository);
  }

  @Bean
  public StreamlabsAlertService streamlabsAlertService(StreamlabsOauthService streamlabsOauthService,
                                                       RestTemplate restTemplate) {
    return new StreamlabsAlertService(streamlabsOauthService, restTemplate);
  }

  @Bean
  public StreamlabsPointsService streamlabsPointsService(StreamlabsOauthService streamlabsOauthService,
                                                         BotConfigurationProperties botConfigurationProperties,
                                                         RestTemplate restTemplate) {
    return new StreamlabsPointsService(streamlabsOauthService, botConfigurationProperties, restTemplate);
  }
}
