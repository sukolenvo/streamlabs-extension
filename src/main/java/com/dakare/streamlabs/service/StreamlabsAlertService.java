package com.dakare.streamlabs.service;

import com.dakare.streamlabs.domain.ImageAlert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
public class StreamlabsAlertService {

    private final StreamlabsOauthService streamlabsOauthService;
    private final RestTemplate restTemplate;

    public boolean showImageAlert(ImageAlert imageAlert) {
        final Optional<String> accessToken = streamlabsOauthService.getStreamlabsAccessToken();
        if (!accessToken.isPresent()) {
            return false;
        }
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("access_token", accessToken.get());
        requestBody.add("type", imageAlert.getType().name().toLowerCase());
        requestBody.add("image_href", imageAlert.getImageUrl());
        requestBody.add("duration", String.valueOf(imageAlert.getDurationMs()));
        if (StringUtils.isNotEmpty(imageAlert.getSoundUrl())) {
            requestBody.add("sound_href", imageAlert.getSoundUrl());
        }
        if (StringUtils.isEmpty(imageAlert.getMessage())) {
            requestBody.add("message", " ");
        } else {
            requestBody.add("message", imageAlert.getMessage());
        }
        if (StringUtils.isEmpty(imageAlert.getUserMessage())) {
            requestBody.add("user_message", " ");
        } else {
            requestBody.add("user_message", imageAlert.getUserMessage());
        }
        if (StringUtils.isNotEmpty(imageAlert.getTextColor())) {
            requestBody.add("special_text_color", imageAlert.getTextColor());
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        try {
            return restTemplate.postForEntity("https://www.streamlabs.com/api/v1.0/alerts",
                    new HttpEntity<>(requestBody, headers), String.class).getStatusCode() == HttpStatus.OK;
        } catch (Exception e) {
            log.warn("Failed to create Streamlabs alert", e);
            return false;
        }
    }
}
