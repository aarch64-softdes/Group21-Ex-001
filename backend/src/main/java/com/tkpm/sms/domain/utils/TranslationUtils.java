package com.tkpm.sms.domain.utils;

import com.tkpm.sms.domain.valueobject.TextContent;
import com.tkpm.sms.domain.valueobject.Translation;

import java.util.List;

public class TranslationUtils {
    public static String getDefaultText(TextContent textContent) {
        if (textContent == null || textContent.getTranslations() == null
                || textContent.getTranslations().isEmpty()) {
            return null;
        }

        List<Translation> translations = textContent.getTranslations();
        return translations.stream().filter(Translation::isOriginal).findFirst()
                .map(Translation::getText).orElse(translations.getFirst().getText());
    }

    public static String getTextByLanguage(TextContent textContent, String languageCode) {
        if (textContent == null || textContent.getTranslations() == null
                || textContent.getTranslations().isEmpty()) {
            return null;
        }

        List<Translation> translations = textContent.getTranslations();
        return translations.stream()
                .filter(translation -> translation.getLanguageCode().equals(languageCode))
                .findFirst().map(Translation::getText).orElseGet(() -> getDefaultText(textContent));
    }
}
