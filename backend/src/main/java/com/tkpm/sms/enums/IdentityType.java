package com.tkpm.sms.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@AllArgsConstructor
public enum IdentityType {
    Identity_Card("Identity Card","[0-9]{9}"),
    Chip_Card("Chip Card","[0-9]{12}"),
    Passport("Passport","[A-Z]{2}[0-9]{7}"),
    ;

    private final String displayName;
    private final String pattern;

    public boolean equals(String value) {
        return displayName.equalsIgnoreCase(value);
    }

    public static IdentityType fromString(String value) {
        for (IdentityType type : IdentityType.values()) {
            if (type.displayName.equalsIgnoreCase(value)) {
                return type;
            }
        }
        return null;
    }
}
