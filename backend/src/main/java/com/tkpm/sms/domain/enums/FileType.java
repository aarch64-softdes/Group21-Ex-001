package com.tkpm.sms.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FileType {
    JSON("json", "application/json"),
    CSV("csv", "text/csv");

    private final String extension;
    private final String mediaType;

    public static FileType fromExtension(String extension) {
        for (FileType type : values()) {
            if (type.getExtension().equalsIgnoreCase(extension)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unsupported file format: " + extension);
    }
}