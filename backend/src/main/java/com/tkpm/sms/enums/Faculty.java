package com.tkpm.sms.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.tkpm.sms.exceptions.ApplicationException;
import com.tkpm.sms.exceptions.ErrorCode;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum Faculty {
    Faculty_of_Japanese("Faculty of Japanese"),
    Faculty_of_French("Faculty of French"),
    Faculty_of_Law("Faculty of Law"),
    Faculty_of_Business_English("Faculty of Business English"),;

    String facultyName;

    @JsonCreator
    public static Faculty fromString(String text) {
        return Arrays.stream(Faculty.values())
                .filter(faculty -> faculty.facultyName.equalsIgnoreCase(text))
                .findFirst()
                .orElseThrow(() -> new ApplicationException(
                    ErrorCode.NOT_FOUND.withMessage(
                        String.format("Invalid faculty: %s", text)
                    )
                ));
    }

    @Override
    @JsonValue
    public String toString() {
        return this.facultyName;
    }
}
