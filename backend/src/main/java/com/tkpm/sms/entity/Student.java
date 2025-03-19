package com.tkpm.sms.entity;

import com.tkpm.sms.enums.Gender;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@Entity
@Table(name = "students")
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(name = "student_Id", unique = true)
    String studentId;

    @Pattern(regexp = "^[a-zA-Z\\s]*$", message = "Name must contain only letters")
    @NotNull
    String name;

    LocalDate dob;

    @Enumerated(EnumType.STRING)
    @Column (nullable = false)
    Gender gender;

    Integer course;

    @Email
    @NotNull
    @Column(unique = true)
    String email;

    String address;

    @NotNull
    @Pattern(regexp = "^0\\d{9}$", message = "Phone number must start with 0 and have 10 digits")
    @Column(unique = true, length = 10)
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
}
