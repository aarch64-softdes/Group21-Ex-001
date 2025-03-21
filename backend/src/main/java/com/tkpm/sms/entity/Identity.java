package com.tkpm.sms.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.tkpm.sms.enums.IdentityType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@Entity
@Table(name = "identities")
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Identity {
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

    @JsonBackReference
    @OneToOne(mappedBy = "identity")
    Student student;
}
