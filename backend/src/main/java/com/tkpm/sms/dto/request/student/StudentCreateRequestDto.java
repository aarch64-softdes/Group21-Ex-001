package com.tkpm.sms.dto.request.student;

import com.tkpm.sms.dto.request.address.AddressCreateRequestDto;
import com.tkpm.sms.dto.request.identity.IdentityCreateRequestDto;
import com.tkpm.sms.validator.required.RequiredConstraint;
import com.tkpm.sms.validator.identity.IdentityConstraint;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
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
    @RequiredConstraint(field = "Student ID")
    String studentId;

    @RequiredConstraint(field = "Student name")
    @Pattern(regexp = "^[\\p{L}\\s]*$", message = "INVALID_NAME")
    String name;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate dob;
    String gender;

    @RequiredConstraint(field = "Faculty")
    String faculty;

    @RequiredConstraint(field = "Course")
    Integer course;

    @RequiredConstraint(field = "Program")
    String program;

    @Email(message = "INVALID_EMAIL")
    @RequiredConstraint(field = "Student email")
    String email;

    AddressCreateRequestDto permanentAddress;

    AddressCreateRequestDto temporaryAddress;
    
    AddressCreateRequestDto mailingAddress;

    @RequiredConstraint(field = "Phone number")
    @Pattern(regexp = "^0\\d{9}$", message = "INVALID_PHONE_NUMBER")
    String phone;

    String countryCode;

    @RequiredConstraint(field = "Status")
    String status;

    @RequiredConstraint(field = "Identity")
    @IdentityConstraint(values = {"Identity Card", "Chip Card", "Passport"})
    @Valid
    IdentityCreateRequestDto identity;
}
