package com.tkpm.sms.application.dto.request.enrollment;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TranscriptUpdateRequestDto {
    String grade;
    Double gpa;
}
