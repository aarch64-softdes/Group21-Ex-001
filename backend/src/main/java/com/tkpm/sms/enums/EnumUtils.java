package com.tkpm.sms.enums;

import java.util.Arrays;

public class EnumUtils {
    public static String[] getNames(Class<? extends Enum<?>> e) {
        return Arrays.stream(e.getEnumConstants())
                .map(enumVal -> enumVal.name().replace("_", " "))
                .toArray(String[]::new);
    }
}
