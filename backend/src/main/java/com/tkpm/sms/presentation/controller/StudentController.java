package com.tkpm.sms.presentation.controller;

import com.tkpm.sms.application.dto.request.student.StudentCollectionRequest;
import com.tkpm.sms.application.dto.request.student.StudentCreateRequestDto;
import com.tkpm.sms.application.dto.request.student.StudentUpdateRequestDto;
import com.tkpm.sms.application.dto.response.common.ApplicationResponseDto;
import com.tkpm.sms.application.dto.response.common.ListResponse;
import com.tkpm.sms.application.dto.response.common.PageDto;
import com.tkpm.sms.application.dto.response.student.StudentDto;
import com.tkpm.sms.application.dto.response.student.StudentMinimalDto;
import com.tkpm.sms.application.mapper.StudentMapper;
import com.tkpm.sms.application.service.interfaces.StudentService;
import com.tkpm.sms.domain.common.PageResponse;
import com.tkpm.sms.domain.model.Student;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/students")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StudentController {
    StudentService studentService;
    StudentMapper studentMapper;

    @GetMapping
    public ResponseEntity<ApplicationResponseDto<ListResponse<StudentMinimalDto>>> getStudents(
            @ModelAttribute StudentCollectionRequest search
    ) {
        PageResponse<Student> pageResponse = studentService.findAll(search);

        // Map domain entities to DTOs
        List<StudentMinimalDto> studentDtos = pageResponse.getContent().stream()
                .map(studentMapper::toStudentMinimalDto)
                .collect(Collectors.toList());

        // Create page info
        var pageDto = PageDto.builder()
                .totalElements(pageResponse.getTotalElements())
                .pageSize(pageResponse.getPageSize())
                .pageNumber(pageResponse.getPageNumber())
                .totalPages(pageResponse.getTotalPages())
                .build();

        // Create response
        var listResponse = ListResponse.<StudentMinimalDto>builder()
                .page(pageDto)
                .data(studentDtos)
                .build();

        return ResponseEntity.ok(ApplicationResponseDto.success(listResponse));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApplicationResponseDto<StudentDto>> getStudentDetail(@PathVariable String id) {
        var student = studentService.getStudentDetail(id);
        var studentDto = studentMapper.toStudentDto(student);
        return ResponseEntity.ok(ApplicationResponseDto.success(studentDto));
    }

    @PostMapping("/")
    public ResponseEntity<ApplicationResponseDto<StudentDto>> createStudent(
            @Valid @RequestBody StudentCreateRequestDto student,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        var newStudent = studentService.createStudent(student);
        var studentDto = studentMapper.toStudentDto(newStudent);
        var locationOfNewUser = uriComponentsBuilder.path("api/students/{id}").buildAndExpand(newStudent.getId()).toUri();

        return ResponseEntity.created(locationOfNewUser).body(ApplicationResponseDto.success(studentDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudentById(@PathVariable String id) {
        studentService.deleteStudentById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApplicationResponseDto<StudentDto>> updateStudent(
            @PathVariable String id,
            @Valid @RequestBody StudentUpdateRequestDto student
    ) {
        var updatedStudent = studentService.updateStudent(id, student);
        var studentDto = studentMapper.toStudentDto(updatedStudent);
        return ResponseEntity.ok(ApplicationResponseDto.success(studentDto));
    }
}