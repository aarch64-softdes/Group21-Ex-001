package com.tkpm.sms.application.dto.response.subject;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubjectDto {
    Integer id;
    String name;
    String code;
    String description;
    Integer credits;
    String faculty;
    List<PrerequisitesSubjects> prerequisitesSubjects;
}
