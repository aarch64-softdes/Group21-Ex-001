package com.tkpm.sms.domain.utils;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ListUtils {
    public static <T, U> List<T> transform(List<U> list, Function<U, T> consumer) {
        return list.stream().map(consumer).collect(Collectors.toList());
    }
}
