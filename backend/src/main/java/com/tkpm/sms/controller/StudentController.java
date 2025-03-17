package com.tkpm.sms.controller;

import com.tkpm.sms.dto.request.StudentCreateRequestDto;
import com.tkpm.sms.dto.request.StudentUpdateRequestDto;
import com.tkpm.sms.dto.response.StudentDto;
import com.tkpm.sms.dto.response.common.ApplicationResponseDto;
import com.tkpm.sms.dto.response.common.ListResponse;
import com.tkpm.sms.dto.response.common.PageDto;
import com.tkpm.sms.mapper.StudentMapper;
import com.tkpm.sms.service.StudentService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/students")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StudentController {
    StudentService studentService;
    StudentMapper studentMapper;

    @GetMapping
    public ResponseEntity<ApplicationResponseDto<ListResponse<StudentDto>>> getStudents(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "studentId") String sortName,
            @RequestParam(defaultValue = "asc") String sortType,
            @RequestParam(defaultValue = "") String search
    ) {
        Page<StudentDto> pageResult =
                studentService.getStudents(page, sortName, sortType, search)
                        .map(studentMapper::toStudentDto);

        var pageDto = PageDto.builder()
                .totalElements(pageResult.getTotalElements())
                .pageSize(pageResult.getSize())
                .pageNumber(pageResult.getNumber())
                .totalPages(pageResult.getTotalPages())
                .build();

        var listResponse = ListResponse.<StudentDto>builder()
                .page(pageDto)
                .data(pageResult.stream().toList())
                .build();

        return ResponseEntity.ok(ApplicationResponseDto.success(listResponse));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApplicationResponseDto<StudentDto>> getStudentDetail(@PathVariable String id) {
        var student = studentService.getStudentDetail(id);
        var response = ApplicationResponseDto.success(
                studentMapper.toStudentDto(student));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/")
    public ResponseEntity<ApplicationResponseDto<StudentDto>> addNewStudent(
            @Valid @RequestBody StudentCreateRequestDto student,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        var newStudent = studentService.addNewStudent(student);

        var response = ApplicationResponseDto.success(studentMapper.toStudentDto(newStudent));
        var locationOfNewUser = uriComponentsBuilder.path("api/students/{id}").buildAndExpand(newStudent.getId()).toUri();
        return ResponseEntity.created(locationOfNewUser).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudentById(@PathVariable String id) {
        studentService.deleteStudentById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApplicationResponseDto<StudentDto>> updateStudent(
            @PathVariable String id,
            @Valid @RequestBody StudentUpdateRequestDto student) {
        var updatedStudent = studentService.updateStudent(id, student);
        var response = ApplicationResponseDto.success(studentMapper.toStudentDto(updatedStudent));
        return ResponseEntity.ok(response);
    }
}
