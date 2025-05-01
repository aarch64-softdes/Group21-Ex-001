package com.tkpm.sms.application.dto.request.student;

import com.tkpm.sms.application.annotation.IdentityConstraint;
import com.tkpm.sms.application.annotation.RequiredConstraint;
import com.tkpm.sms.application.dto.request.address.AddressUpdateRequestDto;
import com.tkpm.sms.application.dto.request.identity.IdentityUpdateRequestDto;
import com.tkpm.sms.application.dto.request.phone.PhoneRequestDto;
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
public class StudentUpdateRequestDto {
    @RequiredConstraint(field = "Student ID")
    String studentId;

    @RequiredConstraint(field = "Student name")
    @Pattern(regexp = "^[\\p{L}\\s]*$", message = "INVALID_NAME")
    String name;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate dob;
    String gender;

    @RequiredConstraint(field = "Faculty")
    Integer faculty;

    @RequiredConstraint(field = "School Year")
    Integer schoolYear;

    @RequiredConstraint(field = "Program")
    Integer program;

    @Email(message = "INVALID_EMAIL")
    @RequiredConstraint(field = "Student email")
    String email;

    AddressUpdateRequestDto permanentAddress;

    AddressUpdateRequestDto temporaryAddress;

    AddressUpdateRequestDto mailingAddress;

    PhoneRequestDto phone;

    @RequiredConstraint(field = "Status")
    Integer status;

    @RequiredConstraint(field = "Identity")
    @IdentityConstraint(values = {"Identity Card", "Chip Card", "Passport"})
    @Valid
    IdentityUpdateRequestDto identity;
}
