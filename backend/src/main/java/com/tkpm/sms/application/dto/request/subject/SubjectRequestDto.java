package com.tkpm.sms.application.dto.request.subject;

import com.tkpm.sms.application.annotation.RequiredConstraint;
import com.tkpm.sms.domain.model.Faculty;
import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubjectRequestDto {
    @RequiredConstraint(field = "name")
    String name;

    @RequiredConstraint(field = "code")
    String code;

    String description;

    @RequiredConstraint(field = "credits")
    Integer credits;

    Integer facultyId;

    List<Integer> prerequisitesId;
}
