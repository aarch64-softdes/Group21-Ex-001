package com.tkpm.sms.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "programs")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@SQLDelete(sql = "UPDATE programs SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class ProgramEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "name_id", unique = true)
    TextContentEntity name;

    @Column(name = "deleted_at")
    LocalDate deletedAt;

    // one-to-many relationship with students
    @OneToMany(mappedBy = "program")
    Set<StudentEntity> students;

    // one-to-many relationship with courses
    @OneToMany(mappedBy = "program", fetch = FetchType.EAGER)
    Set<CourseEntity> courses;
}