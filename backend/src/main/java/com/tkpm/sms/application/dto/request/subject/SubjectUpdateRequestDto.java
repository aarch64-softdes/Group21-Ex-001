package com.tkpm.sms.application.dto.request.subject;

import com.tkpm.sms.application.annotation.RequiredConstraint;
import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubjectUpdateRequestDto {
    @RequiredConstraint(field = "name")
    String name;

    String description;

    @RequiredConstraint(field = "credits")
    @Min(value = 2, message = "INVALID_SUBJECT_CREDITS")
    Integer credits;

    Integer facultyId;

    List<Integer> prerequisitesId;
}
