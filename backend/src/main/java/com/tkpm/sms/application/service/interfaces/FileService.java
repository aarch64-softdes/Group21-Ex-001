package com.tkpm.sms.application.service.interfaces;

public interface FileService {
    byte[] exportStudentFile(String format);
    void importStudentFile(String format, Object multipartFile);
}