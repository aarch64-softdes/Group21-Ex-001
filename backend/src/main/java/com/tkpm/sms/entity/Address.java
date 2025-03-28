package com.tkpm.sms.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "addresses")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String street;
    String ward;
    String district;
    String province;
    String country;

    @JsonBackReference
    @OneToOne(mappedBy = "permanentAddress")
    Student student;

    @Override
    public String toString() {
        return street + ", " + ward + ", " + district + ", "  + province + ", " + country;
    }
}
