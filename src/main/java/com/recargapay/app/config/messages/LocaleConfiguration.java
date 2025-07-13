package com.recargapay.app.config.messages;

import java.util.Locale;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

@Configuration
public class LocaleConfiguration {

    private static final String DEFAULT_LOCALE = "pt-BR";

    @Bean
    public LocaleResolver localeResolver() {
        var localeResolver = new AcceptHeaderLocaleResolver();
        localeResolver.setDefaultLocale(Locale.forLanguageTag(DEFAULT_LOCALE));
        return localeResolver;
    }
}
