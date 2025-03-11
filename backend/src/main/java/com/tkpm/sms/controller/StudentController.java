package com.tkpm.sms.controller;

import com.tkpm.sms.dto.StudentRequest;
import com.tkpm.sms.dto.StudentResponse;
import com.tkpm.sms.entity.Student;
import com.tkpm.sms.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;

    @GetMapping("/")
    public ResponseEntity<List<StudentResponse>> getAllStudents() {
        List<StudentResponse> students = studentService.getAllStudents();
        return ResponseEntity.ok(students);
    }

    @PostMapping("/")
    public ResponseEntity<?> addNewStudent(@Valid @RequestBody StudentRequest student) {
        studentService.addNewStudent(student);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudentById(@PathVariable String id)
    {
        studentService.deleteStudentById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStudent(@PathVariable String id,  @RequestBody StudentRequest student)
    {
        studentService.updateStudent(id, student);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<StudentResponse>> getStudentsByNameOrId(@RequestParam String find) {
        List<StudentResponse> students = studentService.getStudentsByNameOrId(find);
        return ResponseEntity.ok(students);
    }
}
