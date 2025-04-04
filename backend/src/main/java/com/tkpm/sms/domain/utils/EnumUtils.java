package com.tkpm.sms.domain.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnumUtils {

    public static String[] getNames(Class<? extends Enum<?>> e) {
        return Arrays.stream(e.getEnumConstants())
                .map(enumVal ->
                        enumVal.name().replace("_", " "))
                .toArray(String[]::new);
    }

    public static <T extends Enum<T>> List<Map<String, String>> getNamesAndValues(Class<T> e) {
        return Arrays.stream(e.getEnumConstants())
                .map(enumVal -> {
                    Map<String, String> pair = new HashMap<>();
                    pair.put("name", enumVal.name());
                    pair.put("value", enumVal.name().replace("_", " "));
                    return pair;
                })
                .toList();
    }
}
