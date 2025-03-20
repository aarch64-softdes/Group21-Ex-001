package com.tkpm.sms.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IdentityCreateRequestDto {
    @NotNull(message = "NULL_VALUE")
    String type;

    @NotNull(message = "NULL_VALUE")
    String number;

    @NotNull(message = "NULL_VALUE")
    String issuedBy;

    @NotNull(message = "NULL_VALUE")
    LocalDate issuedDate;

    @NotNull(message = "NULL_VALUE")
    LocalDate expiryDate;
}
