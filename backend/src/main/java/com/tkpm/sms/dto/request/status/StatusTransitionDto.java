package com.tkpm.sms.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StatusTransitionDTO {
    Integer id;
    
    @NotNull(message = "From status ID is required")
    Integer fromStatusId;
    
    @NotNull(message = "To status ID is required")
    Integer toStatusId;
    
    // optional fields
    String fromStatusName;
    String toStatusName;
}