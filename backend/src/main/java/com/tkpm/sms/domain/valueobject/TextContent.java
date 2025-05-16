package com.tkpm.sms.domain.valueobject;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TextContent {
    Integer id;
    LocalDateTime createdAt;
    List<Translation> translations;
}
