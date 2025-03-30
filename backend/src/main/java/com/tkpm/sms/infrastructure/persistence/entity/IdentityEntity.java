package com.tkpm.sms.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Table(name = "identities")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IdentityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(nullable = false)
    String type;

    @Column(nullable = false)
    String number;

    @Column(nullable = false)
    String issuedBy;

    @Column(nullable = false)
    LocalDate issuedDate;

    @Column(nullable = false)
    LocalDate expiryDate;

    // For chip-based
    @Column(name = "has_chip")
    boolean hasChip;

    // For passport
    String country;
    String notes;

//    @OneToOne(mappedBy = "identity")
//    StudentEntity student;
}