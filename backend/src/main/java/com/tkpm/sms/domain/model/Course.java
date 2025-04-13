package com.tkpm.sms.domain.model;

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
    String lecturer;
    int maxStudent;
    String room;
    String schedule;
    LocalDate startDate;

    Program program;
    Subject subject;
}
