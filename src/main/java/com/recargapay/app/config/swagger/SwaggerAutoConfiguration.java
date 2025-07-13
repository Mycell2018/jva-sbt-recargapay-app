package com.recargapay.app.config.swagger;

import com.recargapay.app.config.security.properties.SecurityProperties;
import com.recargapay.app.config.swagger.properties.SwaggerProperties;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.security.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Slf4j
@Configuration
@RequiredArgsConstructor
@ConditionalOnClass(OpenAPI.class)
@EnableConfigurationProperties({SwaggerProperties.class, SecurityProperties.class})
@ConditionalOnProperty(name = "commons.api.swagger.enabled", havingValue = "true")
@PropertySource({"classpath:messages/swagger_defaults_en.properties"})
public class SwaggerAutoConfiguration {

    private final SwaggerProperties swaggerProperties;
    private final SecurityProperties securityProperties;
    private final Environment environment;

    @Bean
    @ConditionalOnMissingBean
    public OpenAPI openApiConfig() {
        boolean enabled = environment.getProperty("commons.api.swagger.enabled", Boolean.class, false);

        if (!enabled) {
            log.info("\uD83D\uDFE0 Swagger desativado!");
            log.info(
                    "\uD83D\uDFE6 Para ativar o Swagger, adicione a propriedade 'commons.api.swagger.enabled=true' no arquivo application.properties ou application.yaml.");
            return null;
        } else {
            log.info("\uD83D\uDFE2 Swagger ativado.");
            log.info(
                    "\uD83D\uDFE6 Para desativar o Swagger, adicione a propriedade 'commons.api.swagger.enabled=false' no arquivo application.properties ou application.yaml.");
        }

        OpenAPI openAPI = new OpenAPI();

        Components components = new Components();

        if (securityProperties != null && securityProperties.getJwt() != null) {
            SecurityScheme securityOauth2Scheme = createSecurityOauth2Scheme();
            SecurityScheme securityBearerToken = createSecurityBearerToken();

            components.addSecuritySchemes("oauth2", securityOauth2Scheme);
            components.addSecuritySchemes("bearerAuth", securityBearerToken);

            openAPI.addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
        }

        Info info = new Info();
        License license = new License();
        ExternalDocumentation externalDocumentation = new ExternalDocumentation();

        if (swaggerProperties.getLicense() != null) {
            license.name(swaggerProperties.getLicense().getName());
            license.url(swaggerProperties.getLicense().getUrl());

            info.license(license);
        }

        if (swaggerProperties.getTitle() != null) {
            info.title(swaggerProperties.getTitle());
        }

        if (swaggerProperties.getDescription() != null) {
            info.description(swaggerProperties.getDescription());
        }

        if (swaggerProperties.getVersion() != null) {
            info.version(swaggerProperties.getVersion());
        }

        if (swaggerProperties.getContact() != null) {
            info.contact(new io.swagger.v3.oas.models.info.Contact()
                    .email(swaggerProperties.getContact().getEmail())
                    .name(swaggerProperties.getContact().getName())
                    .url(swaggerProperties.getContact().getUrl()));
        }

        if (swaggerProperties.getExternalDocs() != null) {
            externalDocumentation.setDescription(
                    swaggerProperties.getExternalDocs().getDescription());
            externalDocumentation.setUrl(swaggerProperties.getExternalDocs().getUrl());
        }

        return openAPI.info(info).externalDocs(externalDocumentation).components(components);
    }

    private SecurityScheme createSecurityOauth2Scheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.OAUTH2)
                .openIdConnectUrl(securityProperties.getJwt().getJwk())
                .flows(new OAuthFlows()
                        .clientCredentials(new OAuthFlow()
                                .tokenUrl(securityProperties.getJwt().getJwk())
                                .scopes(new Scopes().addString("trust", "trust all"))));
    }

    private SecurityScheme createSecurityBearerToken() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .name("bearerAuth")
                .scheme("bearer")
                .bearerFormat("JWT");
    }

    @Bean
    public OperationCustomizer customizeGlobalHeaders() {
        return (operation, handlerMethod) -> {
            operation.addParametersItem(new Parameter()
                    .in("header")
                    .name("Accept-Language")
                    .description("Request Language (Ex: pt-BR, en)")
                    .required(false)
                    .example("pt-BR")
                    .schema(new io.swagger.v3.oas.models.media.StringSchema()));

            return operation;
        };
    }
}
