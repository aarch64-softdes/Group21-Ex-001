package com.tkpm.sms.application.service.implementation;

import com.tkpm.sms.application.service.interfaces.TextContentService;
import com.tkpm.sms.domain.repository.TextContentRepository;
import com.tkpm.sms.domain.valueobject.TextContent;
import com.tkpm.sms.domain.valueobject.Translation;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class TextContentServiceImpl implements TextContentService {
    TextContentRepository textContentRepository;

    @NonFinal
    @Value("${app.locale.default}")
    String DEFAULT_LANGUAGE;

    @Override
    public TextContent createTextContent(String text) {
        var languageCode = LocaleContextHolder.getLocale().getLanguage();

        var textContent = TextContent.builder().createdAt(LocalDateTime.now())
                .translations(Collections
                        .singletonList(Translation.builder().languageCode(languageCode).text(text)
                                .isOriginal(languageCode.equals(DEFAULT_LANGUAGE)).build()))
                .build();

        return textContentRepository.save(textContent);
    }

    @Override
    public TextContent updateTextContent(TextContent existingContent, String text) {
        if (existingContent == null) {
            return createTextContent(text);
        }

        String languageCode = LocaleContextHolder.getLocale().getLanguage();
        List<Translation> translations = existingContent.getTranslations();

        if (translations == null) {
            translations = new ArrayList<>();
            existingContent.setTranslations(translations);
        }

        // Look for existing translation in this language
        boolean found = false;
        for (Translation translation : translations) {
            if (translation.getLanguageCode().equals(languageCode)) {
                translation.setText(text);
                found = true;
                break;
            }
        }

        // If no translation exists for this language, add one
        if (!found) {
            Translation newTranslation = Translation.builder().text(text).languageCode(languageCode)
                    .isOriginal(languageCode.equals(DEFAULT_LANGUAGE)).build();

            translations.add(newTranslation);
        }

        return textContentRepository.save(existingContent);
    }
}
