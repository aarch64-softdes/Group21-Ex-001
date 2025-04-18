package com.tkpm.sms.domain.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class History {
    String id;
    Student student;
    Course course;
    ActionType actionType;
    LocalDateTime createdAt;

    @Getter
    public enum ActionType {
        ENROLLED, DELETED;
    }
}
