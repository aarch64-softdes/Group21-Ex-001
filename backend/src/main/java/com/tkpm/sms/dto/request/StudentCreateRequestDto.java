package com.tkpm.sms.dto.request;

import com.tkpm.sms.enums.Status;
import com.tkpm.sms.validator.StatusConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StudentCreateRequestDto {
    @NotNull(message = "Student ID is required")
    String studentId;

    @NotNull(message = "Student name is required")
    @Pattern(regexp = "^[a-zA-Z\\s]*$", message = "INVALID_NAME")
    String name;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate dob;
    String gender;
    String faculty;
    Integer course;
    String program;

    @Email(message = "INVALID_EMAIL")
    @NotNull(message = "Student email is required")
    String email;

    AddressCreateRequestDto permanentAddress;
    AddressCreateRequestDto temporaryAddress;
    AddressCreateRequestDto mailingAddress;

    @NotNull(message = "Student phone number is required")
    @Pattern(regexp = "^0\\d{9}$", message = "INVALID_PHONE")
    String phone;

    @StatusConstraint(message = "INVALID_STATUS")
    String status = Status.Studying.name();

    @NotNull
    String citizenship;
}
