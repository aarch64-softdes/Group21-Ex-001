package com.tkpm.sms.application.dto.response.student;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StudentFileDto {
    //Basic information
    @JsonProperty("Student ID")
    String studentId;
    
    @JsonProperty("Full Name")
    String name;
    
    @JsonProperty("Date of Birth")
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate dob;
    
    @JsonProperty("Gender")
    String gender;

    // Academic Information
    @JsonProperty("Faculty")
    String faculty;
    
    @JsonProperty("Course")
    Integer course;
    
    @JsonProperty("Program")
    String program;
    
    @JsonProperty("Status")
    String status;

    // Contact Information
    @JsonProperty("Email")
    String email;
    
    @JsonProperty("Phone")
    String phone;

    // Address Information
    @JsonProperty("Permanent Address")
    String permanentAddress;
    
    @JsonProperty("Temporary Address")
    String temporaryAddress;
    
    @JsonProperty("Mailing Address")
    String mailingAddress;

    // Identity Information
    @JsonProperty("Identity Type")
    String identityType;
    
    @JsonProperty("Identity Number")
    String identityNumber;
    
    @JsonProperty("Issued By")
    String identityIssuedBy;
    
    @JsonProperty("Issue Date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    String identityIssuedDate;
    
    @JsonProperty("Expiry Date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    String identityExpiryDate;
    
    @JsonProperty("Has Chip")
    boolean identityHasChip;
    
    @JsonProperty("Issuing Country")
    String identityCountry;
    
    @JsonProperty("Additional Notes")
    String identityNotes;
}
