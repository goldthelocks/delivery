package com.eraine.delivery.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "service.rest-config.voucher-api")
public class VoucherApiConfig {
    private String baseUrl;
    private String apiKey;
}
