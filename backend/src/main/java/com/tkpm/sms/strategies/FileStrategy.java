package com.tkpm.sms.strategies;

import org.springframework.web.multipart.MultipartFile;

public interface FileStrategy {
    byte[] export(Iterable<?> data);
    void importFile(MultipartFile file);
    String getFormat();
}
