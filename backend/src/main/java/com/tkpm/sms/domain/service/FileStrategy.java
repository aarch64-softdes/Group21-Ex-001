package com.tkpm.sms.domain.service;

public interface FileStrategy {
    byte[] export(Iterable<?> data);
    void importFile(Object file);
    String getFormat();
}