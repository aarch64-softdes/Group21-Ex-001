package com.tkpm.sms.entity;

import com.tkpm.sms.enums.Faculty;
import com.tkpm.sms.enums.Gender;
import com.tkpm.sms.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

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

    Date dob;

    @Enumerated(EnumType.STRING)
    @Column (nullable = false)
    Gender gender;

    @Enumerated(EnumType.STRING)
    @Column (nullable = false)
    Faculty faculty;

    Integer course;

    String program;

    @Email
    @NotNull
    @Column(unique = true)
    String email;

    String address;

    @NotNull
    @Pattern(regexp = "^0\\d{9}$", message = "Phone number must start with 0 and have 10 digits")
    @Column(unique = true, length = 10)
    String phone;

    @Enumerated(EnumType.STRING)
    @Column (nullable = false)
    Status status = Status.Studying;
}
