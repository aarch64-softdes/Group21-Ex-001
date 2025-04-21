package com.tkpm.sms.infrastructure.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "subjects")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubjectEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @NotNull
    String name;

    @NotNull
    String code;
    @Column(name = "is_active", columnDefinition = "boolean default true")
    boolean isActive;
    String description;

    @NotNull
    Integer credits;

    @Column(name = "created_at")
    LocalDateTime createdAt;

    // many-to-one relationship with faculty
    @ManyToOne
    @JoinColumn(name = "faculty_id")
    FacultyEntity faculty;

    // many-to-many relationship with subject
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "subject_prerequisites", joinColumns = @JoinColumn(name = "subject_id"), inverseJoinColumns = @JoinColumn(name = "prerequisite_id"))
    Set<SubjectEntity> prerequisites;
}
