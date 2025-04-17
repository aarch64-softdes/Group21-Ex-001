package com.tkpm.sms.application.dto.response.enrollment;

import com.tkpm.sms.application.dto.response.course.CourseDto;
import com.tkpm.sms.application.dto.response.course.CourseMinimalDto;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EnrollmentMinimalDto {
    Integer id;
    CourseMinimalDto course;

    TranscriptMinimalDto transcript;
}
