package com.tkpm.sms.infrastructure.file;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tkpm.sms.application.dto.response.student.StudentFileDto;
import com.tkpm.sms.application.service.interfaces.StudentService;
import com.tkpm.sms.domain.exception.ErrorCode;
import com.tkpm.sms.domain.exception.FileProcessingException;
import com.tkpm.sms.domain.service.FileStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Slf4j
@Component("jsonStrategy")
@RequiredArgsConstructor
public class JsonStrategy implements FileStrategy {
    @Qualifier("jsonMapper")
    private final ObjectMapper jsonMapper;
    private final StudentService studentService;

    @Override
    public byte[] export(Iterable<?> data) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            jsonMapper.writerWithDefaultPrettyPrinter().writeValue(outputStream, data);
        } catch (IOException e) {
            log.error("Failed to export file with Json format", e);

            throw new FileProcessingException("Failed to export with csv format", ErrorCode.FAIL_TO_EXPORT_FILE);
        }

        return outputStream.toByteArray();
    }

    @Override
    public void importFile(Object file) {
        if (!(file instanceof MultipartFile multipartFile)) {
            throw new FileProcessingException("Invalid file type", ErrorCode.INVALID_FILE_FORMAT);
        }

        List<StudentFileDto> students;

        try {
            students = jsonMapper.readValue(
                    multipartFile.getInputStream(),
                    jsonMapper.getTypeFactory()
                            .constructCollectionType(List.class, StudentFileDto.class));
        } catch (IOException e) {
            log.info("Error reading file", e);

            throw new FileProcessingException("Error reading file", ErrorCode.FAIL_TO_IMPORT_FILE);
        }

        studentService.saveListStudentFromFile(students);
    }

    @Override
    public String getFormat() {
        return "json";
    }
}