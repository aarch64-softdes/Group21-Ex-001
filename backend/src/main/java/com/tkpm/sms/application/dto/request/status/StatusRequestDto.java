package com.tkpm.sms.application.dto.request.status;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StatusRequestDto {
    @NotEmpty(message = "NOT_NULL;Status's name is required")
    String name;

    List<Integer> validTransitionIds;
}



