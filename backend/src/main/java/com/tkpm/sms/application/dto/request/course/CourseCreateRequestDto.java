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
public class CourseCreateRequestDto {
    int year;
    @RequiredConstraint(field = "Semester")
    int semester;
    @RequiredConstraint(field = "Code")
    String code;
    @RequiredConstraint(field = "Lecturer")
    String lecturer;
    @RequiredConstraint(field = "Max student")
    int maxStudent;
    @RequiredConstraint(field = "Room")
    String room;

    @Valid
    @CourseScheduleConstraint
    @RequiredConstraint(field = "Schedule")
    CourseScheduleDto schedule;

    @RequiredConstraint(field = "Start date")
    LocalDate startDate;

    @RequiredConstraint(field = "Program id")
    Integer programId;

    @RequiredConstraint(field = "Subject id")
    Integer subjectId;
}
