package com.tkpm.sms.application.dto.response.subject;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubjectDto {
    Integer id;
    String name;
    String code;
    String description;
    boolean isActive;
    Integer credits;
    String faculty;
    List<PrerequisiteSubjectDto> prerequisitesSubjects;
}
