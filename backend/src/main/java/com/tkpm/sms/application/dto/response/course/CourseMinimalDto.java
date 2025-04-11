package com.tkpm.sms.application.dto.response.course;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseMinimalDto {
    String code;
    String subject;
    int year;
    int semester;
    String room;
    String schedule;
    LocalDate startDate;
}
