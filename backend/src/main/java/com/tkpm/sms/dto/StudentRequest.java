package com.tkpm.sms.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StudentRequest {
    String studentId;
    String name;
    Date dob;
    String gender;
    String faculty;
    Integer course;
    String program;
    String email;
    String address;
    String phone;
    String status;
}
