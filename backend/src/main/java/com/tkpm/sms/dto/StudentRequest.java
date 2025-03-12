package com.tkpm.sms.dto;

import com.tkpm.sms.enums.Faculty;
import com.tkpm.sms.enums.Gender;
import com.tkpm.sms.enums.Status;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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

    @Email
    @NotNull
    String email;

    String address;

    @NotNull
    @Pattern(regexp = "^0\\d{9}$", message = "Phone number must start with 0 and have 10 digits")
    String phone;

    Status status = Status.Studying;
}
