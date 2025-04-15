package com.tkpm.sms.infrastructure.service;

import com.tkpm.sms.application.dto.request.student.StudentCreateRequestDto;
import com.tkpm.sms.application.dto.response.student.StudentFileDto;
import com.tkpm.sms.application.service.interfaces.FileService;
import com.tkpm.sms.application.service.interfaces.StudentService;
import com.tkpm.sms.domain.exception.ErrorCode;
import com.tkpm.sms.domain.exception.FileProcessingException;
import com.tkpm.sms.domain.exception.ResourceNotFoundException;
import com.tkpm.sms.domain.model.Student;
import com.tkpm.sms.domain.service.validators.StudentDomainValidator;
import com.tkpm.sms.infrastructure.factories.FileStrategyFactory;
import com.tkpm.sms.infrastructure.mapper.StudentMapperImpl;
import com.tkpm.sms.infrastructure.persistence.entity.StudentEntity;
import com.tkpm.sms.infrastructure.persistence.jpa.StudentJpaRepository;
import com.tkpm.sms.infrastructure.persistence.mapper.StudentPersistenceMapper;
import com.tkpm.sms.application.service.interfaces.DocumentTemplateProcessingService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
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
    DocumentTemplateProcessingService documentService;

    @Override
    public byte[] exportStudentFile(String format) {
        List<StudentFileDto> students = new ArrayList<>();
        int pageSize = 100;
        int currentPage = 0;
        Page<StudentEntity> studentPage;

        do {
            studentPage = studentRepository.findAll(
                    PageRequest.of(
                            currentPage,
                            pageSize,
                            Sort.by("studentId")));

            students.addAll(studentPage
                    .getContent()
                    .stream()
                    .map(studentPersistenceMapper::toDomain)
                    .map(studentMapper::toStudentFileDto)
                    .toList());

            currentPage++;
        } while (currentPage < studentPage.getTotalPages());

        return fileStrategyFactory.getStrategy(format).toBytes(students);
    }

    @Override
    public void importStudentFile(String format, Object multipartFile) {
        if (!(multipartFile instanceof MultipartFile)) {
            throw new FileProcessingException("Invalid file type", ErrorCode.INVALID_FILE_FORMAT);
        }

        List<StudentFileDto> students = fileStrategyFactory.getStrategy(format).convert(multipartFile,
                StudentFileDto.class);

        studentService.saveListStudentFromFile(students);
    }

    @Override
    public byte[] exportTranscript(String studentId) {
        try {
            Map<String, Object> data = getStudentTranscriptData(studentId);
            return documentService.processTemplateAsHtmlToPdf("templates/template.html", data);
        } catch (Exception e) {
            log.error("Failed to generate transcript PDF for student {}", studentId, e);
            throw new FileProcessingException("Failed to generate transcript PDF", ErrorCode.FAIL_TO_EXPORT_FILE);
        }
    }

    @Override
    public Map<String, Object> getStudentTranscriptData(String studentId) {
        var student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Student with id %s not found", studentId)));

        var studentDomain = studentPersistenceMapper.toDomain(student);

        Map<String, Object> data = new HashMap<>();
        data.put("studentId", studentDomain.getStudentId());
        data.put("studentName", studentDomain.getName());
        data.put("className", studentDomain.getFaculty().getName());
        data.put("birthDate", studentDomain.getDob().toString());
        data.put("semester", "Spring 2025"); // TODO: Get from current academic term

        // TODO: Calculate actual GPA from student's courses
        data.put("gpa", calculateGPA(studentDomain));

        // TODO: Get actual course data from student's enrollment
        List<Map<String, Object>> subjects = getStudentCourses(studentDomain);
        data.put("subjects", subjects);

        data.put("totalCredits", calculateTotalCredits(subjects));

        return data;
    }

    private Double calculateGPA(Student student) {
        // TODO: Implement actual GPA calculation from student's courses
        return 3.85; // Placeholder
    }

    private List<Map<String, Object>> getStudentCourses(Student student) {
        // TODO: Get actual course data from student's enrollment
        return List.of(
                Map.of(
                        "courseId", "MATH101",
                        "name", "Mathematics",
                        "credits", 4,
                        "finalScore", 85.5),
                Map.of(
                        "courseId", "COMP201",
                        "name", "Data Structures",
                        "credits", 3,
                        "finalScore", 92.0));
    }

    private Integer calculateTotalCredits(List<Map<String, Object>> subjects) {
        return subjects.stream()
                .mapToInt(subject -> (Integer) subject.get("credits"))
                .sum();
    }
}