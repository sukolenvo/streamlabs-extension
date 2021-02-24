package com.dakare.streamlabs.service;

import com.dakare.streamlabs.config.properties.StreamlabsOauthConfigurationProperties;
import com.dakare.streamlabs.domain.AppDbProperty;
import com.dakare.streamlabs.repository.AppDbPropertyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.PostConstruct;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class StreamlabsOauthService {

    private static final String ACCESS_TOKEN_KEY = "streamlabs_access_token";

    private final StreamlabsOauthConfigurationProperties streamlabsOauthConfigurationProperties;
    private final AppDbPropertyRepository appDbPropertyRepository;

    @PostConstruct
    public void init() {
        if (StringUtils.isNotEmpty(streamlabsOauthConfigurationProperties.getAccessToken())) {
            log.info("Saving streamlabs access token in database");
            appDbPropertyRepository.save(new AppDbProperty()
                    .setKey(ACCESS_TOKEN_KEY)
                    .setValue(streamlabsOauthConfigurationProperties.getAccessToken()));
        }
    }

    public Optional<String> getStreamlabsAccessToken() {
        return appDbPropertyRepository.findById(ACCESS_TOKEN_KEY)
                .map(AppDbProperty::getValue);
    }

    public void saveAccessToken(String accessToken) {
        appDbPropertyRepository.save(new AppDbProperty()
                .setKey(ACCESS_TOKEN_KEY)
                .setValue(accessToken));
    }
}
