package com.tkpm.sms.application.dto.response.subject;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PrerequisiteSubjectDto {
    Integer id;
    String name;
    String code;
    String description;
}
