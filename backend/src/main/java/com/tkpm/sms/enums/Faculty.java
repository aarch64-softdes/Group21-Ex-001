package com.tkpm.sms.enums;

import java.util.Arrays;

public enum Faculty {
    Faculty_of_Japanese,
    Faculty_of_French,
    Faculty_of_Law,
    Faculty_of_Business_English;

    @Override
    public String toString() {
        return name().replace("_", " ");
    }
}
