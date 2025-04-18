package com.tkpm.sms.application.dto.response.enrollment;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TranscriptDto {
    String subjectCode;
    String subjectName;
    String grade;
    Double gpa;
}
