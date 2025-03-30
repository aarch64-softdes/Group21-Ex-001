package com.tkpm.sms.domain.model;

import com.tkpm.sms.domain.enums.Gender;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Student {
    String id;
    String studentId;
    String name;
    LocalDate dob;
    Gender gender;
    Integer course;
    String email;
    String phone;

    // Relationships
    Faculty faculty;
    Program program;
    Status status;
    Address permanentAddress;
    Address temporaryAddress;
    Address mailingAddress;
    Identity identity;

    public boolean isStatusTransitionAllowed(Status newStatus) {
        return status != null && status.canTransitionTo(newStatus);
    }
}