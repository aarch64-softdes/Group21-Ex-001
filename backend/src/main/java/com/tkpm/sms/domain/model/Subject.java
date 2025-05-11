package com.tkpm.sms.domain.model;

import com.tkpm.sms.domain.utils.TranslationUtils;
import com.tkpm.sms.domain.valueobject.TextContent;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Subject {
    Integer id;
    TextContent name;
    String code;
    boolean isActive;
    TextContent description;
    LocalDateTime createdAt;
    Integer credits;
    Faculty faculty;
    List<Subject> prerequisites;

    public String getNameByLanguage(String language) {
        return TranslationUtils.getTextByLanguage(name, language);
    }

    public String getDescriptionByLanguage(String language) {
        return TranslationUtils.getTextByLanguage(description, language);
    }
}