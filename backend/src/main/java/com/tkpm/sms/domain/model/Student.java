package com.tkpm.sms.domain.model;

import com.tkpm.sms.domain.enums.Gender;
import com.tkpm.sms.domain.valueobject.Phone;
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
    Integer schoolYear;
    String email;
    Phone phone;
    LocalDate deletedAt;

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