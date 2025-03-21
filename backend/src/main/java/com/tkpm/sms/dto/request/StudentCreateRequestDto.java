package com.tkpm.sms.dto.request;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.tkpm.sms.validator.identity.IdentityConstraint;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StudentCreateRequestDto {
    @NotNull(message = "Student ID is required")
    String studentId;

    @NotNull(message = "Student name is required")
    @Pattern(regexp = "^[\\p{L}\\s]*$", message = "INVALID_NAME")
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

    @NotNull
    String status;

    @NotNull(message = "Identity is required")
    @IdentityConstraint(values = {"Identity Card", "Chip Card", "Passport"})
    @Valid
    IdentityCreateRequestDto identity;
}
