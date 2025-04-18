package com.tkpm.sms.application.dto.request.enrollment;

import com.tkpm.sms.application.annotation.RequiredConstraint;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TranscriptUpdateRequestDto {
    @RequiredConstraint(field = "Grade")
    String grade;

    @RequiredConstraint(field = "Grade")
    Double gpa;
}
