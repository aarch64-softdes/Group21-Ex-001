package com.tkpm.sms.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum IdentityType {
    Identity_Card("Identity Card"),
    ChipBased_Card("Chip-based Card"),
    Passport("Passport"),
    ;

    private final String displayName;

    public boolean equals(String value) {
        return this.displayName.equalsIgnoreCase(value);
    }
}
