package com.tkpm.sms.application.dto.response.enrollment;

import com.tkpm.sms.application.dto.response.course.CourseDto;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EnrollmentHistoryDto {
    String id;
    String actionType;
    CourseDto course;
    LocalDateTime createdAt;
}
