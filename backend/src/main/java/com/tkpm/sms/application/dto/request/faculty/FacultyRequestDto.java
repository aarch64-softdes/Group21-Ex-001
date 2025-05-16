package com.tkpm.sms.application.dto.request.faculty;

import com.tkpm.sms.application.annotation.RequiredConstraint;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FacultyRequestDto {
    @RequiredConstraint(field = "Faculty's name")
    String name;
}
