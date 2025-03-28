package com.tkpm.sms.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.tkpm.sms.enums.Gender;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Table(name = "students")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(name = "student_Id", unique = true)
    String studentId;

    @Pattern(regexp = "^[\\p{L}\\s]*$", message = "Name must contain only letters and spaces")
    @NotNull
    String name;

    LocalDate dob;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    Gender gender;

    Integer course;

    @Email
    @NotNull
    @Column(unique = true)
    String email;

    @NotNull
    @Pattern(regexp = "^\\+\\d{1,3}\\d{6,12}$", message = "Phone number must be in international format (e.g., +84123456789)")
    @Column(unique = true)
    String phone;

    // many-to-one relationship with status
    @ManyToOne
    @JoinColumn(name = "status_id")
    Status status;

    // many-to-one relationship with program
    @ManyToOne
    @JoinColumn(name = "program_id")
    Program program;

    // many-to-one relationship with faculty
    @ManyToOne
    @JoinColumn(name = "faculty_id")
    Faculty faculty;

    @JsonBackReference
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "permanent_address_id")
    Address permanentAddress;

    @JsonBackReference
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "temporary_address_id")
    Address temporaryAddress;

    @JsonBackReference
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "mailing_address_id")
    Address mailingAddress;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    @JoinColumn(name = "identity_id")
    Identity identity;
}
