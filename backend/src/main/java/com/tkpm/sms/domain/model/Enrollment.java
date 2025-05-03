package com.tkpm.sms.domain.model;

import com.tkpm.sms.domain.valueobject.History;
import com.tkpm.sms.domain.valueobject.Score;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Enrollment {
    Integer id;
    Student student;
    Course course;
    History history;
    Score score;
}
