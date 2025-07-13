package com.recargapay.app.config.swagger.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@ConfigurationProperties(prefix = "commons.api.swagger.license")
public class SwaggerLicenseProperties {
    private String name;
    private String url;
}
