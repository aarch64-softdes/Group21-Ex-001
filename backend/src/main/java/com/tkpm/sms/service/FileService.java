package com.tkpm.sms.service;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tkpm.sms.dto.response.student.StudentFileDto;
import com.tkpm.sms.entity.Student;
import com.tkpm.sms.exceptions.ApplicationException;
import com.tkpm.sms.exceptions.ErrorCode;
import com.tkpm.sms.factories.FileExportStrategyFactory;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FileService {
    StudentRepository studentRepository;
    StudentMapper studentMapper;
    StudentService studentService;

    FileExportStrategyFactory fileExportStrategyFactory;

    String JSON_FORMAT = "json";
    String CSV_FORMAT = "csv";

    public byte[] exportStudentFile(String format) {
        List<StudentFileDto> students = new ArrayList<>();
        int pageSize = 100;
        int currentPage = 0;
        Page<Student> studentPage;
        do {
            studentPage = studentRepository.findAll(
                    PageRequest.of(currentPage, pageSize, Sort.by("studentId"))
            );
            students.addAll(studentPage.getContent().stream()
                    .map(studentMapper::toStudentFileDto)
                    .toList());
            currentPage++;
        } while (currentPage < studentPage.getTotalPages());

        return fileExportStrategyFactory.getStrategy(format).export(students);
    }

    public void importStudentFile(String format, MultipartFile multipartFile) {
        switch (format) {
            case JSON_FORMAT -> importStudentsFromJson(multipartFile);
            case CSV_FORMAT -> importStudentsFromCsv(multipartFile);
            default ->
                    throw new ApplicationException(ErrorCode.INVALID_FILE_FORMAT.withMessage("Unsupported file format " + format));
        }
    }

    private void importStudentsFromJson(MultipartFile file) {
        var jsonMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        List<StudentFileDto> students;
        try {
            students = jsonMapper.readValue(file.getInputStream(),
                    jsonMapper.getTypeFactory().constructCollectionType(List.class, StudentFileDto.class));
        } catch (IOException e) {
            log.info("Error reading file", e);
            throw new ApplicationException(ErrorCode.FAIL_TO_IMPORT_FILE.withMessage("Fail to import students data from JSON"));
        }
        log.info("Students: {}", Arrays.toString(students.toArray()));
        saveStudents(students);
    }

    private void importStudentsFromCsv(MultipartFile file) {
        CsvMapper csvMapper = new CsvMapper();
        csvMapper.configure(
                MapperFeature.SORT_PROPERTIES_ALPHABETICALLY,
                false
        );
        csvMapper.findAndRegisterModules();
        csvMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        CsvSchema csvSchema = csvMapper.typedSchemaFor(StudentFileDto.class).withSkipFirstDataRow(true);
        log.info("CSV Schema: {}", csvSchema);
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
        log.info("Students: {}", Arrays.toString(students.toArray()));
        saveStudents(students);
    }

    private void saveStudents(List<StudentFileDto> students) {
        var studentCreateRequests = students.stream().map(studentMapper::toStudentCreateRequest).toList();
        for (var student : studentCreateRequests) {
            studentService.createStudent(student);
        }
    }
}
