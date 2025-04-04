package com.tkpm.sms.application.dto.request.identity;

import com.tkpm.sms.application.annotation.RequiredConstraint;
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
    @RequiredConstraint(field = "Identity type")
    String type;

    @RequiredConstraint(field = "Identity number")
    String number;

    @RequiredConstraint(field = "Identity issued agency")
    String issuedBy;

    @RequiredConstraint(field = "Identity issued date")
    LocalDate issuedDate;

    @RequiredConstraint(field = "Identity expiry date")
    LocalDate expiryDate;

    // For chip-based
    boolean hasChip = false;

    // For passport
    String country;

    String notes;
}
