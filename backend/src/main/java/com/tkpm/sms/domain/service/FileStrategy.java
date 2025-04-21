package com.tkpm.sms.domain.service;

import java.util.List;

public interface FileStrategy {
    byte[] toBytes(Iterable<?> data);

    <T> List<T> convert(Object file, Class<T> clazz);

    String getFormat();
}