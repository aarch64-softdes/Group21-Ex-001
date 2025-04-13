package com.tkpm.sms.domain.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Subject {
    Integer id;
    String name;
    String code;
    boolean isActive;
    String description;
    LocalDateTime createdAt;
    Integer credits;
    Faculty faculty;
    List<Subject> prerequisites;
}