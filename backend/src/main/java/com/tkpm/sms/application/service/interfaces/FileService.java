package com.tkpm.sms.application.service.interfaces;

import java.util.Map;

public interface FileService {
    byte[] exportStudentFile(String format);

    void importStudentFile(String format, Object multipartFile);

    byte[] exportTranscript(Map<String, Object> data);

    void importTranscriptFile(String format, Object multipartFile);
}