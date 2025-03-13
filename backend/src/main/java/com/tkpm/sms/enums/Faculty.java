package com.tkpm.sms.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Faculty {
    Faculty_of_Japanese("Faculty of Japanese"),
    Faculty_of_French("Faculty of French"),
    Faculty_of_Law("Faculty of Law"),
    Faculty_of_Business_English("Faculty of Business English"),;

    private String facultyName;


    Faculty(String facultyName) {
        this.facultyName = facultyName;
    }

    @JsonCreator
    public static Faculty fromString(String text) {
        return Arrays.stream(Faculty.values())
                .filter(faculty -> faculty.facultyName.equalsIgnoreCase(text))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No constant with text " + text + " found"));
    }

    @Override
    @JsonValue
    public String toString() {
        return this.facultyName;
    }
}
