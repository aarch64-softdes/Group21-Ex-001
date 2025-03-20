package com.tkpm.sms.strategies;

public interface FileExportStrategy {
    byte[] export(Iterable<?> data);
    String getFormat();
}
