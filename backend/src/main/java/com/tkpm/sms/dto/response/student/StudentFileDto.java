package com.tkpm.sms.dto.response.student;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StudentFileDto {
    String studentId;
    String name;
    LocalDate dob;
    String gender;
    String faculty;
    Integer course;
    String program;
    String email;
    String phone;
    String status;

    String permanentAddress;
    String temporaryAddress;
    String mailingAddress;

    String identityType;
    String identityNumber;
    String identityIssuedBy;
    String identityIssuedDate;
    String identityExpiryDate;

    String identityNotes;
    String identityCountry;
    boolean identityHasChip;
}
