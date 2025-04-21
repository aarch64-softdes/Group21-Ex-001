package com.tkpm.sms.infrastructure.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "settings")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SettingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Pattern(regexp = "^[a-z]+$")
    @Column(unique = true, nullable = false, updatable = false)
    String name;

    @Column(columnDefinition = "TEXT")
    String details;
}