package com.tkpm.sms.domain.model;

import com.tkpm.sms.domain.utils.TranslationUtils;
import com.tkpm.sms.domain.valueobject.TextContent;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Faculty {
    Integer id;
    TextContent name;
    LocalDate deletedAt;
    Set<Student> students;
    Set<Subject> subjects;

    public String getDefaultName() {
        return TranslationUtils.getDefaultText(name);
    }

    public String getNameByLanguage(String language) {
        return TranslationUtils.getTextByLanguage(name, language);
    }
}