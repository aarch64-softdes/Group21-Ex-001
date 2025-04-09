package com.tkpm.sms.infrastructure.persistence.entity;

import com.tkpm.sms.domain.model.Faculty;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

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
    String name;
    String code;
    String description;
    Integer credits;

    // many-to-one relationship with faculty
    @ManyToOne
    @JoinColumn(name = "faculty_id")
    FacultyEntity faculty;

    // relationship with prerequisite subjects
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "subject_prerequisites",
            joinColumns = @JoinColumn(name = "subject_id")
    )
    @Column(name = "prerequisite_id")
    List<Integer> prerequisitesId;
}
