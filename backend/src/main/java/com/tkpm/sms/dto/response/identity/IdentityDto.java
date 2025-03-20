package com.tkpm.sms.dto.response.identity;

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
public class IdentityDto {
    String type;
    String number;
    String issuedBy;
    LocalDate issuedDate;
    LocalDate expiryDate;
}
