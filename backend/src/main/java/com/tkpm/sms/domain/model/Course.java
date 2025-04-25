package com.tkpm.sms.domain.model;

import com.tkpm.sms.domain.valueobject.Schedule;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Course {
    Integer id;
    int year;
    int semester;
    String code;
    String lecturer;
    int maxStudent;
    String room;
    Schedule schedule;
    LocalDate startDate;

    Program program;
    Subject subject;
}
