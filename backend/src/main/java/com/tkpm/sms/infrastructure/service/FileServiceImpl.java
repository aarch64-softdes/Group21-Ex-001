package com.tkpm.sms.infrastructure.service;

import com.tkpm.sms.application.dto.request.enrollment.EnrollmentFileImportDto;
import com.tkpm.sms.application.dto.response.student.StudentFileDto;
import com.tkpm.sms.application.service.interfaces.EnrollmentService;
import com.tkpm.sms.application.service.interfaces.FileService;
import com.tkpm.sms.application.service.interfaces.StudentService;
import com.tkpm.sms.domain.exception.ErrorCode;
import com.tkpm.sms.domain.exception.FileProcessingException;
import com.tkpm.sms.domain.service.validators.StudentDomainValidator;
import com.tkpm.sms.infrastructure.factories.FileStrategyFactory;
import com.tkpm.sms.infrastructure.mapper.StudentMapperImpl;
import com.tkpm.sms.infrastructure.persistence.entity.StudentEntity;
import com.tkpm.sms.infrastructure.persistence.jpa.StudentJpaRepository;
import com.tkpm.sms.infrastructure.persistence.mapper.StudentPersistenceMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FileServiceImpl implements FileService {
    StudentJpaRepository studentRepository;

    StudentPersistenceMapper studentPersistenceMapper;
    StudentMapperImpl studentMapper;
    StudentService studentService;
    FileStrategyFactory fileStrategyFactory;

    StudentDomainValidator studentDomainValidator;
    EnrollmentService enrollmentService;
    DocumentTemplateProcessingService documentService;

    @Override
    public byte[] exportStudentFile(String format) {
        List<StudentFileDto> students = new ArrayList<>();
        int pageSize = 100;
        int currentPage = 0;
        Page<StudentEntity> studentPage;

        do {
            studentPage = studentRepository
                    .findAll(PageRequest.of(currentPage, pageSize, Sort.by("studentId")));

            students.addAll(
                    studentPage.getContent().stream().map(studentPersistenceMapper::toDomain)
                            .map(studentMapper::toStudentFileDto).toList());

            currentPage++;
        } while (currentPage < studentPage.getTotalPages());

        return fileStrategyFactory.getStrategy(format).toBytes(students);
    }

    @Override
    public void importStudentFile(String format, Object multipartFile) {
        if (!(multipartFile instanceof MultipartFile)) {
            throw new FileProcessingException(ErrorCode.INVALID_FILE_FORMAT,
                    "error.file_processing.invalid_file_format");
        }

        List<StudentFileDto> students = fileStrategyFactory.getStrategy(format)
                .convert(multipartFile, StudentFileDto.class);

        studentService.saveListStudentFromFile(students);
    }

    @Override
    public void importTranscriptFile(String format, Object multipartFile) {
        if (!(multipartFile instanceof MultipartFile)) {
            throw new FileProcessingException(ErrorCode.INVALID_FILE_FORMAT,
                    "error.file_processing.invalid_file_format");
        }

        List<EnrollmentFileImportDto> transcripts = fileStrategyFactory.getStrategy(format)
                .convert(multipartFile, EnrollmentFileImportDto.class);

        enrollmentService.updateTranscripts(transcripts);
    }

    @Override
    public byte[] exportTranscript(Map<String, Object> data) {
        try {
            return documentService.processTemplateAsHtmlToPdf("templates/template.html", data);
        } catch (Exception e) {
            log.error("Failed to generate transcript PDF for student {}", data.get("studentId"), e);
            throw new FileProcessingException(ErrorCode.FAIL_TO_EXPORT_FILE,
                    "error.file_processing.generate_failed", "PDF");
        }
    }
}