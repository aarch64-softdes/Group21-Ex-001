package com.tkpm.sms.infrastructure.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Table(name = "students")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StudentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(name = "student_Id", unique = true)
    String studentId;

    @Pattern(regexp = "^[\\p{L}\\s]*$", message = "Name must contain only letters and spaces")
    @NotNull
    String name;

    LocalDate dob;

    @Column(nullable = false)
    String gender;

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
    StatusEntity status;

    // many-to-one relationship with program
    @ManyToOne
    @JoinColumn(name = "program_id")
    ProgramEntity program;

    // many-to-one relationship with faculty
    @ManyToOne
    @JoinColumn(name = "faculty_id")
    FacultyEntity faculty;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "permanent_address_id")
    AddressEntity permanentAddress;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "temporary_address_id")
    AddressEntity temporaryAddress;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "mailing_address_id")
    AddressEntity mailingAddress;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "identity_id")
    IdentityEntity identity;
}