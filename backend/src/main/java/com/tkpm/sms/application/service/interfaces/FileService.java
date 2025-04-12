package com.tkpm.sms.application.service.interfaces;

import org.springframework.web.bind.annotation.PathVariable;

public interface FileService {
    byte[] exportStudentFile(String format);

    void importStudentFile(String format, Object multipartFile);

    byte[] exportTranscript(@PathVariable String id);
}