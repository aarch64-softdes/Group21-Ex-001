package com.tkpm.sms.dto.request.status;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StatusVerificationDto {
    @NotEmpty(message = "NOT_NULL;fromId is required")
    Integer fromId;

    @NotEmpty(message = "NOT_NULL;toId is required")
    Integer toId;
}



