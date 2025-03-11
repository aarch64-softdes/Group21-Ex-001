package com.tkpm.sms.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tkpm.sms.Enum.Faculty;
import com.tkpm.sms.Enum.Gender;
import com.tkpm.sms.Enum.Status;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StudentResponse {
    String id;
    String studentId;
    String name;
    Date dob;
    Gender gender;
    Faculty faculty;
    Integer course;
    String program;
    String email;
    String address;
    String phone;
    Status status;
}
