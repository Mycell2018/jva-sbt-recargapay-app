package com.recargapay.app.config.security.properties;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

@Data
@Component
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "commons.api.security")
public class SecurityProperties {
    private Boolean enabled;

    private List<String> allowedOrigins;

    private List<String> allowedUrls;

    private List<String> roles;

    private String allowedClaim;

    @NestedConfigurationProperty
    private SecurityJwtProperties jwt;
}
