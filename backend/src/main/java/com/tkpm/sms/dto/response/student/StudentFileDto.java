package com.tkpm.sms.dto.response.student;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonPropertyOrder({
        "id", "studentId", "name", "dob", "gender", "faculty", "course", "program",
        "email", "phone", "status",
        "permanentStreet", "permanentWard", "permanentDistrict", "permanentCountry",
        "temporaryStreet", "temporaryWard", "temporaryDistrict", "temporaryCountry",
        "mailingStreet", "mailingWard", "mailingDistrict", "mailingCountry"
})
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
    String notes;
    String country;
    boolean hasChip;
}
