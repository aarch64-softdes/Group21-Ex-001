package com.tkpm.sms.domain.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Program {
    Integer id;
    String name;
    LocalDate deletedAt;
    Set<Student> students;
}