package com.homelyassist.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "msg91")
@Getter
@Setter
public class MSG91Config {

    private String templateId;

    private String authKey;

    private boolean enabled;

}
