package com.tkpm.sms.domain.enums;

import com.tkpm.sms.domain.exception.ErrorCode;
import com.tkpm.sms.domain.exception.InvalidIdentityException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.regex.Pattern;

@Getter
@RequiredArgsConstructor
public enum IdentityType {
    IDENTITY_CARD("Identity Card", Pattern.compile("[0-9]{9}")),
    CHIP_CARD("Chip Card", Pattern.compile("[0-9]{12}")),
    PASSPORT("Passport", Pattern.compile("[A-Z]{2}[0-9]{7}"));

    private final String displayName;
    private final Pattern validationPattern;

    public static IdentityType fromDisplayName(String displayName) {
        return Arrays.stream(values()).filter(type -> type.getDisplayName().equals(displayName))
                .findFirst().orElseThrow(() -> new InvalidIdentityException(
                        "Unknown identity type: " + displayName, ErrorCode.INVALID_IDENTITY_TYPE));
    }

    public boolean isValidNumber(String number) {
        return validationPattern.matcher(number).matches();
    }
}