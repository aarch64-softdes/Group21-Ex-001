package com.tkpm.sms.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Builder
@Getter
@Setter
@Entity
@Table(name = "citizenships")
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Citizenship {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    @Column(name = "country_name")
    String countryName;

    @OneToMany(mappedBy = "citizenship")
    List<Student> students;
}
