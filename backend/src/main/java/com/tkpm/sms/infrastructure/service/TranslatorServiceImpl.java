package com.tkpm.sms.infrastructure.service;

import com.tkpm.sms.domain.service.TranslatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TranslatorServiceImpl implements TranslatorService {
    private final String DOMAIN_PREFIX = "domain.object.";
    private final MessageSource messageSource;

    @Override
    public String getEntityTranslatedName(Class<?> entityClass) {
        String messageKey = DOMAIN_PREFIX + entityClass.getSimpleName().toLowerCase();
        return messageSource.getMessage(messageKey, null, messageKey,
                LocaleContextHolder.getLocale());
    }
}
