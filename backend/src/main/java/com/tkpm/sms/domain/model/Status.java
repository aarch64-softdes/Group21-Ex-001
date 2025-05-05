package com.tkpm.sms.domain.model;

import com.tkpm.sms.domain.valueobject.TextContent;
import com.tkpm.sms.domain.valueobject.Translation;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Slf4j
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Status {
    Integer id;
    TextContent name;
    LocalDate deletedAt;
    Set<Student> students;
    List<Integer> validTransitionIds;

    public boolean canTransitionTo(Integer targetStatusId) {
        return validTransitionIds != null && validTransitionIds.contains(targetStatusId);
    }

    public boolean canTransitionTo(Status targetStatus) {
        return canTransitionTo(targetStatus.getId());
    }

    public String getDefaultName() {
        log.info("Getting name by default language");
        if (name == null || name.getTranslations().isEmpty()) {
            return null;
        }
        var translations = name.getTranslations();

        var defaultName = translations.stream().filter(Translation::isOriginal).findFirst()
                .map(Translation::getText).orElse(translations.getFirst().getText());
        log.info("Default name: {}", defaultName);

        return defaultName;
    }

    public String getNameByLanguage(String language) {
        log.info("Getting name by language: language={}", language);
        if (name == null || name.getTranslations().isEmpty()) {
            return null;
        }

        var translations = name.getTranslations();

        return translations.stream()
                .filter(translation -> translation.getLanguageCode().equals(language)).findFirst()
                .map(Translation::getText).orElse(getDefaultName());
    }
}