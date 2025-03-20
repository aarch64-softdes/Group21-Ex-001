package com.tkpm.sms.dto.request;

import jakarta.validation.constraints.NotNull;
import jdk.jfr.BooleanFlag;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@Slf4j
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IdentityCreateRequestDto {
    @NotNull(message = "IDENTITY_TYPE_REQUIRED")
    String type;

    @NotNull(message = "NUMBER_REQUIRED")
    String number;

    @NotNull(message = "ISSUED_BY_REQUIRED")
    String issuedBy;

    @NotNull(message = "ISSUED_DATE_REQUIRED")
    LocalDate issuedDate;

    @NotNull(message = "EXPIRY_DATE_REQUIRED")
    LocalDate expiryDate;

    // For chip-based
    @BooleanFlag
    boolean hasChip = false;

    // For passport
    String country;
    String notes;
}
