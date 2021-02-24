package com.dakare.streamlabs.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("app.streamlabs-oauth")
public class StreamlabsOauthConfigurationProperties {

    private String clientId;
    private String clientSecret;
    private String redirectUrl;
    private String accessToken;
    private String scope;
}
