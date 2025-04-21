package com.tkpm.sms.presentation.controller;

import com.tkpm.sms.application.dto.request.student.StudentCollectionRequest;
import com.tkpm.sms.application.dto.request.student.StudentCreateRequestDto;
import com.tkpm.sms.application.dto.request.student.StudentUpdateRequestDto;
import com.tkpm.sms.application.dto.response.common.ApplicationResponseDto;
import com.tkpm.sms.application.dto.response.student.StudentDto;
import com.tkpm.sms.application.dto.response.student.StudentMinimalDto;
import com.tkpm.sms.application.service.interfaces.EnrollmentService;
import com.tkpm.sms.application.service.interfaces.FileService;
import com.tkpm.sms.application.service.interfaces.StudentService;
import com.tkpm.sms.domain.common.PageResponse;
import com.tkpm.sms.domain.model.Student;
import com.tkpm.sms.domain.utils.ListUtils;
import com.tkpm.sms.infrastructure.mapper.StudentMapperImpl;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/students")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StudentController {
    StudentService studentService;
    StudentMapperImpl studentMapper;
    FileService fileService;
    EnrollmentService enrollmentService;

    @GetMapping({"", "/"})
    public ResponseEntity<ApplicationResponseDto<PageResponse<StudentMinimalDto>>> getStudents(
            @ModelAttribute StudentCollectionRequest search) {
        PageResponse<Student> pageResponse = studentService.findAll(search);
        List<StudentMinimalDto> studentDtos = ListUtils.transform(pageResponse.getData(),
                studentMapper::toStudentMinimalDto);
        PageResponse<StudentMinimalDto> listResponse = PageResponse.of(pageResponse, studentDtos);
        return ResponseEntity.ok(ApplicationResponseDto.success(listResponse));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApplicationResponseDto<StudentDto>> getStudentDetail(
            @PathVariable String id) {
        var student = studentService.getStudentDetail(id);
        var studentDto = studentMapper.toStudentDto(student);
        return ResponseEntity.ok(ApplicationResponseDto.success(studentDto));
    }

    @PostMapping({"/", ""})
    public ResponseEntity<ApplicationResponseDto<StudentDto>> createStudent(
            @Valid @RequestBody StudentCreateRequestDto student,
            UriComponentsBuilder uriComponentsBuilder) {
        var newStudent = studentService.createStudent(student);
        var studentDto = studentMapper.toStudentDto(newStudent);
        var locationOfNewUser = uriComponentsBuilder.path("api/students/{id}")
                .buildAndExpand(newStudent.getId()).toUri();

        return ResponseEntity.created(locationOfNewUser)
                .body(ApplicationResponseDto.success(studentDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudentById(@PathVariable String id) {
        studentService.deleteStudentById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApplicationResponseDto<StudentDto>> updateStudent(@PathVariable String id,
            @Valid @RequestBody StudentUpdateRequestDto student) {
        var updatedStudent = studentService.updateStudent(id, student);
        var studentDto = studentMapper.toStudentDto(updatedStudent);
        return ResponseEntity.ok(ApplicationResponseDto.success(studentDto));
    }

    @GetMapping("/{id}/transcript")
    public ResponseEntity<byte[]> getTranscript(@PathVariable String id) {
        try {
            var actualData = enrollmentService.getAcademicTranscriptOfStudent(id);
            var data = Map.of("studentId", actualData.getStudentId(), "studentName",
                    actualData.getStudentName(), "courseName", actualData.getCourseName(),
                    "studentDob", actualData.getStudentDob(), "gpa", actualData.getGpa(),
                    "transcriptList", actualData.getTranscriptList());
            byte[] pdfBytes = fileService.exportTranscript(data);

            String timestamp = new SimpleDateFormat("yyyy-MM-dd_HHmmss").format(new Date());
            String filename = "academic_record_" + id + "_" + timestamp + ".pdf";

            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=" + filename)
                    .contentType(MediaType.APPLICATION_PDF).body(pdfBytes);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate transcript from HTML template", e);
        }
    }
}