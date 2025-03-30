package com.tkpm.sms.infrastructure.file;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.tkpm.sms.application.dto.response.student.StudentFileDto;
import com.tkpm.sms.application.exception.ApplicationException;
import com.tkpm.sms.domain.exception.ErrorCode;
import com.tkpm.sms.application.service.interfaces.StudentService;
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
@Component("csvStrategy")
@RequiredArgsConstructor
public class CsvStrategy implements FileStrategy {
    @Qualifier("csvMapper")
    private final CsvMapper csvMapper;
    private final CsvSchema csvSchema;
    private final StudentService studentService;

    @Override
    public byte[] export(Iterable<?> data) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            csvMapper.writer(csvSchema).writeValue(outputStream, data);
        } catch (IOException e) {
            log.error("Failed to export with csv format", e);

            throw new ApplicationException(ErrorCode.FAIL_TO_EXPORT_FILE.withMessage("Failed to export file with CSV format"));
        }

        return outputStream.toByteArray();
    }

    @Override
    public void importFile(Object file) {
        if (!(file instanceof MultipartFile multipartFile)) {
            throw new ApplicationException(ErrorCode.INVALID_FILE_FORMAT.withMessage("Invalid file format"));
        }

        List<StudentFileDto> students;
        try {
            students = csvMapper.readerFor(StudentFileDto.class)
                    .with(csvSchema)
                    .<StudentFileDto>readValues(multipartFile.getInputStream())
                    .readAll().stream().toList();

        } catch (IOException e) {
            log.info("Error reading file", e);

            throw new ApplicationException(ErrorCode.FAIL_TO_IMPORT_FILE.withMessage("Fail to import students data from CSV"));
        }

        studentService.saveListStudentFromFile(students);
    }

    @Override
    public String getFormat() {
        return "csv";
    }
}