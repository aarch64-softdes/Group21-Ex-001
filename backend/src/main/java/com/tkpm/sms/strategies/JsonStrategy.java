package com.tkpm.sms.strategies;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tkpm.sms.dto.response.student.StudentFileDto;
import com.tkpm.sms.exceptions.ApplicationException;
import com.tkpm.sms.exceptions.ErrorCode;
import com.tkpm.sms.service.StudentService;
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

            throw new ApplicationException(ErrorCode.FAIL_TO_EXPORT_FILE.withMessage("Failed to export file with Json format"));
        }

        return outputStream.toByteArray();
    }

    @Override
    public void importFile(MultipartFile file) {
        List<StudentFileDto> students;

        try {
            students = jsonMapper.readValue(
                    file.getInputStream(),
                    jsonMapper.getTypeFactory()
                            .constructCollectionType(List.class, StudentFileDto.class));
        } catch (IOException e) {
            log.info("Error reading file", e);

            throw new ApplicationException(ErrorCode.FAIL_TO_IMPORT_FILE.withMessage("Fail to import students data from JSON"));
        }

        studentService.saveListStudentFromFile(students);
    }

    @Override
    public String getFormat() {
        return "json";
    }
}
