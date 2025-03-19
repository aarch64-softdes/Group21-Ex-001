package com.tkpm.sms.dto.response.student;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.tkpm.sms.entity.Address;
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
})public class StudentFileExportDto {
    String id;
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

    // Permanent address
    String permanentStreet;
    String permanentWard;
    String permanentDistrict;
    String permanentCountry;

    // Temporary address
    String temporaryStreet;
    String temporaryWard;
    String temporaryDistrict;
    String temporaryCountry;

    // Mailing address
    String mailingStreet;
    String mailingWard;
    String mailingDistrict;
    String mailingCountry;
}
