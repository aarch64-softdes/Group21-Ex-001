package com.tkpm.sms.dto.request.faculty;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FacultyRequestDto {
    @NotEmpty(message = "NOT_NULL;Faculty's name is required")
    String name;
}
