package com.recargapay.app.config.security.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "commons.api.security.jwt")
public class SecurityJwtProperties {
    private String issuer;
    private String jwk;
}
