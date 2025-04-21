package com.tkpm.sms.application.dto.request.enrollment;

import com.tkpm.sms.application.annotation.RequiredConstraint;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EnrollmentCreateRequestDto {
    @RequiredConstraint(field = "studentId", message = "Student ID is required")
    String studentId;

    @RequiredConstraint(field = "courseId", message = "Course ID is required")
    Integer courseId;
}
