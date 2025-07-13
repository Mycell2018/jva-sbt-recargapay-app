package com.recargapay.app.config.security;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

import com.recargapay.app.config.security.properties.SecurityProperties;
import com.recargapay.app.config.swagger.properties.SwaggerProperties;
import io.swagger.v3.oas.models.OpenAPI;
import jakarta.servlet.http.HttpServletRequest;
import java.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity
@ConditionalOnClass(OpenAPI.class)
@EnableConfigurationProperties(SwaggerProperties.class)
// @ConditionalOnProperty(name = "commons.api.security.enabled", havingValue = "true")
public class SecurityAutoConfiguration {

    private final SecurityProperties securityProperties;

    private final SecurityJwtConverter securityJwtConverter;

    private final Environment environment;

    @Bean
    @ConditionalOnMissingBean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        boolean enabled = environment.getProperty("commons.api.security.enabled", Boolean.class, false);

        if (!enabled) {
            log.info("\uD83D\uDFE0 Security desativado!");
            log.info(
                    "\uD83D\uDFE6 Para ativar a segurança, adicione a propriedade 'commons.api.security.enabled=true' no arquivo application.properties ou application.yaml.");
        } else {
            log.info("\uD83D\uDFE2 Security ativado!");
            log.info(
                    "\uD83D\uDFE6 Para desativar a segurança, adicione a propriedade 'commons.api.security.enabled=false' no arquivo application.properties ou application.yaml.");
        }

        String allowedClaim = this.securityProperties.getAllowedClaim();
        String issuer = this.securityProperties.getJwt().getIssuer();
        String jwk = this.securityProperties.getJwt().getJwk();
        List<String> roles = this.securityProperties.getRoles();

        final List<AntPathRequestMatcher> allowedUrls = this.getAllowedUrls();

        http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(
                        oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(customJwtAuthenticationConverter())))
                .headers(httpSecurityHeadersConfigurer ->
                        httpSecurityHeadersConfigurer.frameOptions(frameOptionsConfig -> frameOptionsConfig
                                .sameOrigin()
                                .httpStrictTransportSecurity(
                                        d -> d.includeSubDomains(true).maxAgeInSeconds(31536000L))));

        if (securityProperties.getEnabled() != null && !securityProperties.getEnabled()) {
            http.authorizeHttpRequests(a -> a.anyRequest().permitAll());
        } else {
            log.debug("Authorized Claim: {}", allowedClaim);
            log.debug("JWK: {}", jwk);
            log.debug("Issuer: {}", issuer);
            log.debug("Roles: {}", roles);

            if (!allowedUrls.isEmpty()) {
                http.authorizeHttpRequests(authorize -> allowedUrls.forEach(
                        allowedUrl -> authorize.requestMatchers(allowedUrl).permitAll()));
            }

            if (roles != null && !roles.isEmpty()) {
                http.authorizeHttpRequests(a -> a.anyRequest().hasAnyRole(roles.toArray(String[]::new)));
            } else {
                http.authorizeHttpRequests(a -> a.anyRequest().authenticated());
            }
        }

        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter customJwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(securityJwtConverter);
        return converter;
    }

    @Bean
    protected JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withJwkSetUri(this.securityProperties.getJwt().getJwk())
                .jwsAlgorithm(SignatureAlgorithm.RS256)
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        log.debug("Starting CORS Configuration");
        CorsConfiguration config = new CorsConfiguration();
        log.debug("Allowed Origins: {}", this.securityProperties.getAllowedOrigins());

        config.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        config.setExposedHeaders(List.of("Authorization"));
        config.setAllowCredentials(true);

        if (this.securityProperties.getAllowedOrigins() != null
                && !this.securityProperties.getAllowedOrigins().isEmpty()) {
            config.setAllowedOrigins(this.securityProperties.getAllowedOrigins());
        } else {
            log.warn("No CORS origins configured.");
            config.setAllowedOriginPatterns(List.of("*")); // use isso para permitir todos
        }

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    private List<AntPathRequestMatcher> getAllowedUrls() {
        if (this.securityProperties.getEnabled() != null && !this.securityProperties.getEnabled()) {
            log.debug("+================== ATENÇÃO ==================+");
            log.debug("|");
            log.debug("| SPRING SECURITY SE ENCONTRA DESABILITADO");
            log.debug("|");
            log.debug("+=============================================+");

            return new ArrayList<>();
        }

        log.debug("Starting load of allowed urls");

        List<AntPathRequestMatcher> allowedPaths = new ArrayList<>();

        List<String> allowedUrls = this.securityProperties.getAllowedUrls();

        if (allowedUrls == null) {
            allowedUrls = new ArrayList<>();
        }

        allowedUrls.add("GET:/error");

        for (String url : allowedUrls) {
            if (url.contains(":")) {
                String[] compositePath = url.split(":");
                allowedPaths.add(antMatcher(HttpMethod.valueOf(compositePath[0]), compositePath[1]));
                log.debug("Allowed URL: {} - {}", compositePath[0], compositePath[1]);
            } else {
                allowedPaths.add(antMatcher(url));
                log.debug("Allowed URL: ANY - {}", url);
            }
        }

        return allowedPaths;
    }
}
