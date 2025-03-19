package com.tkpm.sms.dto.response.student;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tkpm.sms.entity.Address;
import com.tkpm.sms.entity.Citizenship;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StudentDto {
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

    Address permanentAddress;
    Address temporaryAddress;
    Address mailingAddress;

    String citizenship;
}
