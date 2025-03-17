package com.tkpm.sms.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tkpm.sms.enums.Status;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Date;

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
    String address;
    String phone;
    String status;
}
