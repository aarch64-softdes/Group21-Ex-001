package com.tkpm.sms.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SequenceWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tkpm.sms.dto.response.student.StudentFileExportDto;
import com.tkpm.sms.entity.Student;
import com.tkpm.sms.exceptions.ApplicationException;
import com.tkpm.sms.exceptions.ErrorCode;
import com.tkpm.sms.mapper.StudentMapper;
import com.tkpm.sms.repository.StudentRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FileService {
    StudentRepository studentRepository;
    StudentMapper studentMapper;

    String JSON_FORMAT = "json";
    String CSV_FORMAT = "csv";

    public byte[] export(String format) {
        return switch (format) {
            case JSON_FORMAT -> exportStudentsToJson();
            case CSV_FORMAT -> exportStudentsToCsv();
            default ->
                    throw new ApplicationException(ErrorCode.INVALID_FILE_FORMAT.withMessage("Unsupported file format"));
        };
    }

    private byte[] exportStudentsToJson() {
        try {
            var jsonMapper = new ObjectMapper()
                    .registerModule(new JavaTimeModule())
                    .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            var writer = jsonMapper.writerWithDefaultPrettyPrinter().writeValues(outputStream);
            writeToOutputStream(writer, JSON_FORMAT);

            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new ApplicationException(ErrorCode.FAIL_TO_EXPORT_FILE.withMessage("Failed to export students data to JSON"));
        }
    }

    private byte[] exportStudentsToCsv() {
        try {
            CsvMapper csvMapper = new CsvMapper();
            csvMapper.findAndRegisterModules();
            csvMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            CsvSchema csvSchema = csvMapper
                    .schemaFor(StudentFileExportDto.class)
                    .withHeader();

            var writer = csvMapper.writer(csvSchema).writeValues(outputStream);
            writeToOutputStream(writer, CSV_FORMAT);

            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new ApplicationException(ErrorCode.FAIL_TO_EXPORT_FILE.withMessage(e.getMessage()));
        }
    }

    private void writeToOutputStream(
            SequenceWriter writer,
            String format
    ) throws IOException {
        int pageSize = 100;
        int currentPage = 0;
        Page<Student> studentPage;
        do {
            studentPage = studentRepository.findAll(PageRequest.of(currentPage, pageSize, Sort.by("studentId")));
            for (var student : studentPage) {
                if (JSON_FORMAT.equals(format)) {
                    writer.write(studentMapper.toStudentDto(student));
                } else if (CSV_FORMAT.equals(format)) {
                    writer.write(studentMapper.toStudentFileExportDto(student));
                } else {
                    throw new ApplicationException(ErrorCode.INVALID_FILE_FORMAT.withMessage("Unsupported file format "));
                }
            }
            currentPage++;
        } while (currentPage < studentPage.getTotalPages());
    }
}
