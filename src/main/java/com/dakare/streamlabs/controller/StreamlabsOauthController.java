package com.dakare.streamlabs.controller;

import com.dakare.streamlabs.config.properties.StreamlabsOauthConfigurationProperties;
import com.dakare.streamlabs.service.StreamlabsOauthService;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/streamlabs")
public class StreamlabsOauthController {

    @Autowired
    private StreamlabsOauthService streamlabsOauthService;

    @Autowired
    private StreamlabsOauthConfigurationProperties streamlabsOauthConfigurationProperties;

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("/auth/status")
    public boolean getAuthConfigStatus() {
        return streamlabsOauthService.getStreamlabsAccessToken().isPresent();
    }

    @GetMapping("/authorize")
    public void startOauth(HttpServletResponse response) throws IOException {
        Preconditions.checkArgument(StringUtils.isNotEmpty(streamlabsOauthConfigurationProperties.getClientId()),
                "streamlabs client_id is missing in application.properties");
        Preconditions.checkArgument(StringUtils.isNotEmpty(streamlabsOauthConfigurationProperties.getClientSecret()),
                "streamlabs client_secret is missing in application.properties");
        Preconditions.checkArgument(StringUtils.isNotEmpty(streamlabsOauthConfigurationProperties.getScope()),
                "streamlabs scope is missing in application.properties");
        Preconditions.checkArgument(StringUtils.isNotEmpty(streamlabsOauthConfigurationProperties.getRedirectUrl()),
                "streamlabs redirect url is missing in application.properties");
        response.sendRedirect("https://www.streamlabs.com/api/v1.0/authorize?response_type=code" +
                "&client_id=" + streamlabsOauthConfigurationProperties.getClientId() +
                "&scope=" + streamlabsOauthConfigurationProperties.getScope() +
                "&redirect_uri=" + streamlabsOauthConfigurationProperties.getRedirectUrl());
    }

    @GetMapping("/auth/callback")
    public void completeOauth(@RequestParam String code, HttpServletResponse response) throws IOException {
        Preconditions.checkArgument(StringUtils.isNotEmpty(streamlabsOauthConfigurationProperties.getClientId()),
                "streamlabs client_id is missing in application.properties");
        Preconditions.checkArgument(StringUtils.isNotEmpty(streamlabsOauthConfigurationProperties.getClientSecret()),
                "streamlabs client_secret is missing in application.properties");
        Preconditions.checkArgument(StringUtils.isNotEmpty(streamlabsOauthConfigurationProperties.getRedirectUrl()),
                "streamlabs redirect url is missing in application.properties");
        Preconditions.checkArgument(StringUtils.isNotEmpty(code), "code request param is mandatory");

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", "authorization_code");
        requestBody.add("client_id", streamlabsOauthConfigurationProperties.getClientId());
        requestBody.add("client_secret", streamlabsOauthConfigurationProperties.getClientSecret());
        requestBody.add("redirect_uri", streamlabsOauthConfigurationProperties.getRedirectUrl());
        requestBody.add("code", code);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        OauthResponse oauthResponse = restTemplate.postForObject("https://www.streamlabs.com/api/v1.0/token",
                new HttpEntity<>(requestBody, headers), OauthResponse.class);
        streamlabsOauthService.saveAccessToken(oauthResponse.getAccessToken());
        response.sendRedirect("/");
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class OauthResponse {
        @JsonProperty("access_token")
        private String accessToken;
        @JsonProperty("refresh_token")
        private String refreshToken;
    }
}
