package com.tkpm.sms.domain.valueobject;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

import com.tkpm.sms.domain.model.Course;
import com.tkpm.sms.domain.model.Student;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EnrollmentHistory {
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
