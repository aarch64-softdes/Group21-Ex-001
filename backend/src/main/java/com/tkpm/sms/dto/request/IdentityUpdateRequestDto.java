package com.tkpm.sms.dto.request;

import com.tkpm.sms.validator.RequiredConstraint;
import jakarta.validation.constraints.NotEmpty;
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

    boolean hasChip = false;

    // For passport
    String country;
    String notes;
}
