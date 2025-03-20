package com.tkpm.sms.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StudentFileImportDto {
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
}
