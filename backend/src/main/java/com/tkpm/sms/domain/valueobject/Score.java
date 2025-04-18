package com.tkpm.sms.domain.valueobject;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Score {
    Integer id;
    String grade;
    Double gpa;
}
