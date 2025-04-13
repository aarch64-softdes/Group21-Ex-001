package com.tkpm.sms.application.dto.response.course;

import com.tkpm.sms.application.annotation.RequiredConstraint;
import com.tkpm.sms.application.dto.request.course.CourseScheduleDto;
import com.tkpm.sms.application.dto.response.ProgramDto;
import com.tkpm.sms.application.dto.response.subject.SubjectDto;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseDto {
    Integer id;
    String code;
    String subject;
    String subjectCode;
    String program;
    String lecturer;
    int credits;
    int year;
    int semester;
    int maxStudent;
    String room;
    String schedule;
    LocalDate startDate;
}
