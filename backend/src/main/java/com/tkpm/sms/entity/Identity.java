package com.tkpm.sms.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    String type;
    String number;
    String issuedBy;
    LocalDate issuedDate;
    LocalDate expiryDate;

    @JsonBackReference
    @OneToOne(mappedBy = "identity")
    Student student;
}
