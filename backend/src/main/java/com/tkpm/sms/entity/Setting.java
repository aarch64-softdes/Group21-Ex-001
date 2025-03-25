package com.tkpm.sms.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
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

    String name;

    //@Lob
    @Column(columnDefinition = "TEXT")
    String details;

    public void setDetails(List<String> detailsList) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            this.details = objectMapper.writeValueAsString(detailsList);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing details", e);
        }
    }

    public List<String> getDetails() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(this.details, List.class);
        } catch (IOException e) {
            throw new RuntimeException("Error deserializing details", e);
        }
    }
}
