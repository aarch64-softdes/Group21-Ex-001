package com.tkpm.sms.application.dto.response.student;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tkpm.sms.application.dto.response.AddressDto;
import com.tkpm.sms.application.dto.response.PhoneDto;
import com.tkpm.sms.application.dto.response.identity.IdentityDto;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StudentDto {
    // Basic Information
    String id;
    String studentId;
    String name;
    LocalDate dob;
    String gender;
    
    // Academic Information
    String faculty;
    Integer course;
    String program;
    String status;
    
    // Contact Information
    String email;
    PhoneDto phone;
    
    // Address Information
    AddressDto permanentAddress;
    AddressDto temporaryAddress;
    AddressDto mailingAddress;
    
    // Identity Information
    IdentityDto identity;
}
