package com.tkpm.sms.controller;

import com.tkpm.sms.dto.request.StudentRequest;
import com.tkpm.sms.dto.reponse.StudentResponse;
import com.tkpm.sms.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;

    @GetMapping
    public ResponseEntity<List<StudentResponse>> getStudents(@RequestParam(defaultValue = "1") int page,
                                                                @RequestParam(defaultValue = "studentId") String sortName,
                                                                @RequestParam(defaultValue = "asc") String sortType,
                                                                @RequestParam(defaultValue = "") String search) {
        List<StudentResponse> students = studentService.getStudents(page, sortName, sortType, search);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponse> getStudentDetail(@PathVariable String id)
    {
        var student = studentService.getStudentDetail(id);
        return ResponseEntity.ok(student);
    }

    @PostMapping("/")
    public ResponseEntity<?> addNewStudent(@Valid @RequestBody StudentRequest student) {
        var newStudent = studentService.addNewStudent(student);
        return ResponseEntity.status(HttpStatus.CREATED).body(newStudent);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudentById(@PathVariable String id)
    {
        studentService.deleteStudentById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateStudent(@PathVariable String id,  @RequestBody StudentRequest student)
    {
        var updatedStudent = studentService.updateStudent(id, student);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(updatedStudent);
    }
}
