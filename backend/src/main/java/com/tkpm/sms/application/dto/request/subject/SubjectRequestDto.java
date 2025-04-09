package com.tkpm.sms.application.dto.request.subject;

import com.tkpm.sms.application.annotation.RequiredConstraint;
import com.tkpm.sms.domain.model.Faculty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubjectRequestDto {
    @RequiredConstraint(field = "Subject's name")
    String name;

    @RequiredConstraint(field = "Subject's code")
    String code;

    String description;

    @RequiredConstraint(field = "Subject's credits")
    Integer credits;

    Integer facultyId;

    List<String> prerequisites;
}
