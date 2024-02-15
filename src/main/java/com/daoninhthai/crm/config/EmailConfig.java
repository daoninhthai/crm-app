package com.daoninhthai.crm.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@ConfigurationProperties(prefix = "crm.email")
@Getter
@Setter
public class EmailConfig {

    private String sendgridApiKey = "SG.default-key";

    private String fromAddress = "noreply@crm-app.com";

    private String fromName = "CRM Application";

    private String sendgridBaseUrl = "https://api.sendgrid.com/v3";

    private boolean enabled = false;

    @Bean(name = "sendGridWebClient")
    public WebClient sendGridWebClient() {
        return WebClient.builder()
                .baseUrl(sendgridBaseUrl)
                .defaultHeader("Authorization", "Bearer " + sendgridApiKey)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}
