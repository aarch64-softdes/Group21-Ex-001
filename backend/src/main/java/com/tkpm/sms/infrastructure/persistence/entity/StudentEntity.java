package com.tkpm.sms.infrastructure.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "students")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@NamedEntityGraph(name = "student-entity-graph", attributeNodes = {@NamedAttributeNode("status"),
        @NamedAttributeNode("program"), @NamedAttributeNode("faculty"),
        @NamedAttributeNode("permanentAddress"), @NamedAttributeNode("temporaryAddress"),
        @NamedAttributeNode("mailingAddress"), @NamedAttributeNode("identity")})
@SQLDelete(sql = "UPDATE students SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
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

    @Column(name = "school_year")
    Integer schoolYear;

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

    @OneToOne
    @JoinColumn(name = "permanent_address_id")
    AddressEntity permanentAddress;

    @OneToOne
    @JoinColumn(name = "temporary_address_id")
    AddressEntity temporaryAddress;

    @OneToOne
    @JoinColumn(name = "mailing_address_id")
    AddressEntity mailingAddress;

    @OneToOne
    @JoinColumn(name = "identity_id")
    IdentityEntity identity;

    @OneToMany(mappedBy = "student")
    List<EnrollmentEntity> enrollments;

    @OneToMany(mappedBy = "student")
    List<HistoryEntity> histories;

    // Soft delete
    @Column(name = "deleted_at")
    LocalDate deletedAt;
}