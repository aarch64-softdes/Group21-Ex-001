package com.tkpm.sms.application.dto.request.course;

import com.tkpm.sms.application.annotation.CourseScheduleConstraint;
import com.tkpm.sms.application.annotation.RequiredConstraint;
import jakarta.validation.Valid;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseUpdateRequestDto {
    @RequiredConstraint(field = "Course year")
    int year;
    @RequiredConstraint(field = "Course semester")
    int semester;
    @RequiredConstraint(field = "Code")
    String code;
    @RequiredConstraint(field = "Lecturer")
    String lecturer;
    @RequiredConstraint(field = "Max student")
    int maxStudent;
    @RequiredConstraint(field = "Course room")
    String room;

    @Valid
    @CourseScheduleConstraint
    @RequiredConstraint(field = "Course schedule")
    CourseScheduleDto schedule;

    @RequiredConstraint(field = "Course start date")
    LocalDate startDate;
}
