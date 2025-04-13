package com.tkpm.sms.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Table(name = "courses")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseEntity {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.SEQUENCE)
    Integer id;
    int year;
    int semester;
    String lecturer;

    @Column(name = "max_student")
    int maxStudent;
    String room;
    String schedule;

    @Column(name = "start_date")
    LocalDate startDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "program_id")
    ProgramEntity program;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "subject_id")
    SubjectEntity subject;
}

