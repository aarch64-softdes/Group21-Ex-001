package com.tkpm.sms.entity;

import jakarta.persistence.*;
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
    String studentId;
    String name;
    Date dob;
    String gender;
    String faculty;
    Integer course;
    String program;
    String email;
    String address;
    String phone;
    String status;
}
