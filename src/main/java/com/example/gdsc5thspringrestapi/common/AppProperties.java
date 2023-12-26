package com.example.gdsc5thspringrestapi.common;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Component
@ConfigurationProperties(prefix = "my-app")
public class AppProperties {
    @NotEmpty
    private String adminUsername;
    @NotEmpty
    private String adminPassword;
    @NotEmpty
    private String userUsername;
    @NotEmpty
    private String userPassword;
    @NotEmpty
    private String clientId;
    @NotEmpty
    private String clientSecret;

}
