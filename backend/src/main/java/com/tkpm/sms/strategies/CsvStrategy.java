package com.tkpm.sms.strategies;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tkpm.sms.dto.response.student.StudentFileDto;
import com.tkpm.sms.exceptions.ApplicationException;
import com.tkpm.sms.exceptions.ErrorCode;
import com.tkpm.sms.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class CsvStrategy implements FileStrategy {
    private final StudentService studentService;
    private CsvMapper csvMapper;
    private CsvSchema csvSchema;

    @Autowired
    public CsvStrategy(StudentService studentService) {
        this.studentService = studentService;
        this.csvMapper = new CsvMapper();
        this.csvMapper.registerModule(new JavaTimeModule());
        this.csvMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        this.csvMapper.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, false);
        this.csvSchema = csvMapper.schemaFor(StudentFileDto.class).withHeader();
    }

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
    public void importFile(MultipartFile file) {
        List<StudentFileDto> students;
        try {
            students = csvMapper.readerFor(StudentFileDto.class)
                    .with(csvSchema)
                    .<StudentFileDto>readValues(file.getInputStream())
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
