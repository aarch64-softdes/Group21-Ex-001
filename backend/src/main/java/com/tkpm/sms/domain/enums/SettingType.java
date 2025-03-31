package com.tkpm.sms.domain.enums;

import lombok.Getter;

@Getter
public enum SettingType {
    EMAIL("email"),
    PHONE_NUMBER("phonenumber");

    private final String value;

    SettingType(String value) {
        this.value = value;
    }
}