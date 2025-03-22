package com.tkpm.sms.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotEmpty;
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
    @NotEmpty(message = "NOT_NULL;Student ID is required")
    String studentId;

    @NotEmpty(message = "NOT_NULL;Student name is required")
    @Pattern(regexp = "^[\\p{L}\\s]*$", message = "INVALID_NAME")
    String name;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate dob;
    String gender;
    String faculty;
    Integer course;
    String program;

    @Email(message = "INVALID_EMAIL")
    @NotEmpty(message = "NOT_NULL;Student email is required")
    String email;

    AddressCreateRequestDto permanentAddress;
    AddressCreateRequestDto temporaryAddress;
    AddressCreateRequestDto mailingAddress;

    @NotEmpty(message = "NOT_NULL;Student phone number is required")
    @Pattern(regexp = "^0\\d{9}$", message = "INVALID_PHONE")
    String phone;

    @NotNull
    String status;

    @NotNull(message = "NOT_NULL;Identity is required")
    @IdentityConstraint(values = {"Identity Card", "Chip Card", "Passport"})
    @Valid
    IdentityCreateRequestDto identity;
}
