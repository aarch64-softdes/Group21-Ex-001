package com.tkpm.sms.application.dto.request.program;

import com.tkpm.sms.application.annotation.RequiredConstraint;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProgramRequestDto {
    @RequiredConstraint(message = "Program's name")
    String name;
}