package com.dakare.streamlabs.service;

import com.dakare.streamlabs.config.properties.BotConfigurationProperties;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class StreamlabsPointsService {

    private final StreamlabsOauthService streamlabsOauthService;
    private final BotConfigurationProperties botConfigurationProperties;
    private final RestTemplate restTemplate;

    public Optional<StreamlabsUserPoints> getUserPoints(String username) {
        String url = "https://streamlabs.com/api/v1.0/points?access_token={accessToken}&username={username}&channel={channel}";
        try {
            return Optional.ofNullable(restTemplate.getForObject(url,
                    StreamlabsUserPoints.class, getAccessToken(), username, botConfigurationProperties.getChannel()));
        } catch (Exception e) {
            log.info("Failed to load loyalty points for user {}", username, e);
            return Optional.empty();
        }
    }

    private String getAccessToken() {
        return streamlabsOauthService.getStreamlabsAccessToken()
                .orElseThrow(() -> new IllegalStateException("Can't make requests to Streamlabs API without access token"));
    }

    public boolean subtractUserPoints(String username, int points) {
        String url = "https://streamlabs.com/api/v1.0/points/subtract";
        try {
            MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
            requestBody.add("access_token", getAccessToken());
            requestBody.add("username", username);
            requestBody.add("channel", botConfigurationProperties.getChannel());
            requestBody.add("points", String.valueOf(points));
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            return restTemplate.postForEntity(url, new HttpEntity<>(requestBody, headers), String.class)
                    .getStatusCode() == HttpStatus.OK;
        } catch (Exception e) {
            log.info("Failed to subscract loyalty points for user {}", username, e);
            return false;
        }
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class StreamlabsUserPoints {
        private String username;
        private long points;
        private String status;
    }
}
