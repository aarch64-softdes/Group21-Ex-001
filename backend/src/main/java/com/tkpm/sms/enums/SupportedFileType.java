package com.tkpm.sms.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SupportedFileType {
    JSON("json", "application/json"),
    CSV("csv", "text/csv");

    private final String extension;
    private final String mediaType;
}
