package com.tkpm.sms.domain.model;

import com.tkpm.sms.domain.enums.IdentityType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Identity {
    String id;
    IdentityType type;
    String number;
    String issuedBy;
    LocalDate issuedDate;
    LocalDate expiryDate;
    boolean hasChip;
    String country;
    String notes;
    Student student;
}