package com.tkpm.sms.dto.request;

import jakarta.validation.constraints.NotNull;
import jdk.jfr.BooleanFlag;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IdentityUpdateRequestDto {
    @NotNull(message = "IDENTITY_TYPE_REQUIRED")
    String type;

    @NotNull(message = "IDENTITY_NUMBER_REQUIRED")
    String number;

    @NotNull(message = "IDENTITY_ISSUED_BY_REQUIRED")
    String issuedBy;

    @NotNull(message = "IDENTITY_ISSUED_DATE_REQUIRED")
    LocalDate issuedDate;

    @NotNull(message = "IDENTITY_EXPIRY_DATE_REQUIRED")
    LocalDate expiryDate;

    boolean hasChip = false;

    // For passport
    String country;
    String notes;
}
