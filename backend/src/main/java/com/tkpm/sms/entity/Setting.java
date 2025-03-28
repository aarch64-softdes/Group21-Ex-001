package com.tkpm.sms.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.IOException;
import java.util.List;

@Entity
@Table(name = "settings")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Setting {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Pattern(regexp = "^[a-z]+$")
    @Column(unique = true, nullable = false, updatable = false)
    String name;

    //@Lob
    @Column(columnDefinition = "TEXT")
    String details;
}
