package com.tkpm.sms.domain.model;

import com.tkpm.sms.domain.utils.TranslationUtils;
import com.tkpm.sms.domain.valueobject.TextContent;
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
        return TranslationUtils.getDefaultText(name);
    }

    public String getNameByLanguage(String language) {
        return TranslationUtils.getTextByLanguage(name, language);
    }
}