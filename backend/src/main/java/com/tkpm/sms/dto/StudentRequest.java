package com.tkpm.sms.dto;

import com.tkpm.sms.Enum.Faculty;
import com.tkpm.sms.Enum.Gender;
import com.tkpm.sms.Enum.Status;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StudentRequest {
    @NotNull(message = "Student ID is required")
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
    Status status = Status.Studying;
}
