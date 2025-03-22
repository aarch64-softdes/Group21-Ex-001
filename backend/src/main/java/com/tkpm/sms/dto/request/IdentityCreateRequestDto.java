package com.tkpm.sms.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jdk.jfr.BooleanFlag;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IdentityCreateRequestDto {
    @NotEmpty(message = "NOT_NULL;Identity type is required")
    String type;

    @NotEmpty(message = "NOT_NULL;Identity number is required")
    String number;

    @NotEmpty(message = "NOT_NULL;Identity issued agency is required")
    String issuedBy;

    @NotNull(message = "NOT_NULL;Identity issued date is required")
    LocalDate issuedDate;

    @NotNull(message = "NOT_NULL;Identity expiry date is required")
    LocalDate expiryDate;

    // For chip-based
    boolean hasChip = false;

    // For passport
    String country;
    String notes;
}
