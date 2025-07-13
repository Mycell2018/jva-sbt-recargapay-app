package com.recargapay.app.config.swagger.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@ConfigurationProperties(prefix = "commons.api.swagger")
public class SwaggerProperties {

    private Boolean enabled;

    private String title;

    private String description;

    private String version;

    @NestedConfigurationProperty
    private SwaggerContactProperties contact;

    @NestedConfigurationProperty
    private SwaggerLicenseProperties license;

    @NestedConfigurationProperty
    private SwaggerExternalDocsProperties externalDocs;
}
