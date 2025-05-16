package com.tkpm.sms.infrastructure.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class LocaleConfig {
    @Value("${app.locale.default}")
    private String DEFAULT_LOCALE;

    @Value("${app.locale.supported}")
    private List<String> SUPPORTED_LANGUAGES;

    @Bean
    public LocaleResolver localeResolver() {
        log.info("Default locale: {}", DEFAULT_LOCALE);

        log.info("Supported locales: {}", SUPPORTED_LANGUAGES);
        List<Locale> supportedLocales = new ArrayList<>();
        for (String lang : SUPPORTED_LANGUAGES) {
            supportedLocales.add(Locale.forLanguageTag(lang));
        }

        AcceptHeaderLocaleResolver resolver = new AcceptHeaderLocaleResolver();
        resolver.setDefaultLocale(java.util.Locale.forLanguageTag(DEFAULT_LOCALE));
        resolver.setSupportedLocales(supportedLocales);

        return resolver;
    }
}
