package com.tkpm.sms.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "histories")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EnrollmentHistoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String actionType;
    LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    StudentEntity student;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    CourseEntity course;
}
