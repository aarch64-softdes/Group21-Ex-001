package com.tkpm.sms.infrastructure.persistence.entity;

import com.tkpm.sms.domain.model.Enrollment;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "transcripts")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TranscriptEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(name = "grade")
    String grade;

    @Column(name = "gpa")
    Double gpa;
}
