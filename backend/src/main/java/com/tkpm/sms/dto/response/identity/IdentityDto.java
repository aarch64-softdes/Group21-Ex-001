package com.tkpm.sms.dto.response.identity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IdentityDto {
    String type;
    String number;
    String issuedBy;
    LocalDate issuedDate;
    LocalDate expiryDate;

    String country;
    String notes;

    boolean hasChip;
}
