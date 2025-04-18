package com.tkpm.sms.application.dto.request.enrollment;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EnrollmentFileImportDto {
    String studentId;

    Integer courseId;

    String grade;

    Double gpa;
}
