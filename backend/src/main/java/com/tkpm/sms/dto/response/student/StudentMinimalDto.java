package com.tkpm.sms.dto.response.student;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;


@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class StudentMinimalDto {
     // Basic Information
     String id;
     String studentId;
     String name;
     LocalDate dob;
     String gender;
     
    // Academic Information
    String faculty;
    Integer course;
    String program;
    String status;

    // Contact Information
    String email;
    String phone;
    String countryCode;
}