package com.tkpm.sms.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum SupportedFileType {
    JSON("json", "application/json"),
    CSV("csv", "text/csv");

    String extension;
    String mediaType;
}
