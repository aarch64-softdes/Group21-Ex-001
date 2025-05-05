package com.tkpm.sms.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "translations")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TranslationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    String text;

    @Column(name = "is_original")
    boolean isOriginal;

    String languageCode;

    @ManyToOne
    @JoinColumn(name = "text_content_id")
    TextContentEntity textContent;
}