package com.tkpm.sms.application.dto.request.program;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProgramRequestDto {
    @NotEmpty(message = "NOT_NULL;Program's name is required")
    String name;
}