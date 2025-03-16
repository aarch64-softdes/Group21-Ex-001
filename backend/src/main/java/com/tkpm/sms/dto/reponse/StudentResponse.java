package com.tkpm.sms.dto.reponse;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tkpm.sms.enums.Status;
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
    String gender;
    String faculty;
    Integer course;
    String program;
    String email;
    String address;
    String phone;
    Status status;
}
