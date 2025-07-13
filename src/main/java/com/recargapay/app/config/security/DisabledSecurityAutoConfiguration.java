package com.recargapay.app.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class DisabledSecurityAutoConfiguration {
    public DisabledSecurityAutoConfiguration(final Environment environment) {}
}
