package com.tkpm.sms.service;

import com.tkpm.sms.dto.StudentRequest;
import com.tkpm.sms.dto.StudentResponse;
import com.tkpm.sms.entity.Student;
import com.tkpm.sms.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final int PAGE_SIZE = 5;

    public List<StudentResponse> getStudents(int page, String sortName, String sortType, String search) {
        Pageable pageable = PageRequest.of(page - 1, PAGE_SIZE,
                                            Sort.by(sortType.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC,
                                            sortName));
        Page<Student> students = studentRepository.getStudents(search, pageable);
        return students.stream().map(this::mapToResponse).toList();
    }

    public StudentResponse addNewStudent(StudentRequest studentRequest) {
        if(studentRepository.existsStudentByStudentId(studentRequest.getStudentId())) {
            throw new RuntimeException("Student ID already exists");
        }
        if(studentRepository.existsStudentByEmail(studentRequest.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        Student student = Student.builder()
                    .studentId(studentRequest.getStudentId())
                    .name(studentRequest.getName())
                    .dob(studentRequest.getDob())
                    .gender(studentRequest.getGender())
                    .faculty(studentRequest.getFaculty())
                    .course(studentRequest.getCourse())
                    .program(studentRequest.getProgram())
                    .email(studentRequest.getEmail())
                    .address(studentRequest.getAddress())
                    .phone(studentRequest.getPhone())
                    .status(studentRequest.getStatus())
                    .build();

        studentRepository.save(student);
        return mapToResponse(student);
    }

    public void deleteStudentById(String id) {
        if(studentRepository.existsStudentByStudentId(id)) {
            throw new RuntimeException("Student not found");
        }
        studentRepository.deleteById(id);
    }

    public StudentResponse updateStudent(String id, StudentRequest studentRequest) {
        var student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        if (studentRequest.getName() != null) student.setName(studentRequest.getName());
        if (studentRequest.getDob() != null) student.setDob(studentRequest.getDob());
        if (studentRequest.getGender() != null) student.setGender(studentRequest.getGender());
        if (studentRequest.getFaculty() != null) student.setFaculty(studentRequest.getFaculty());
        if (studentRequest.getCourse() != null) student.setCourse(studentRequest.getCourse());
        if (studentRequest.getProgram() != null) student.setProgram(studentRequest.getProgram());
        if (studentRequest.getEmail() != null) student.setEmail(studentRequest.getEmail());
        if (studentRequest.getAddress() != null) student.setAddress(studentRequest.getAddress());
        if (studentRequest.getPhone() != null) student.setPhone(studentRequest.getPhone());
        if (studentRequest.getStatus() != null) student.setStatus(studentRequest.getStatus());

        studentRepository.save(student);
        return mapToResponse(student);
    }

    private StudentResponse mapToResponse(Student student) {
        return StudentResponse.builder()
                .id(student.getId())
                .studentId(student.getStudentId())
                .name(student.getName())
                .dob(student.getDob())
                .gender(student.getGender())
                .faculty(student.getFaculty())
                .course(student.getCourse())
                .program(student.getProgram())
                .email(student.getEmail())
                .address(student.getAddress())
                .phone(student.getPhone())
                .status(student.getStatus())
                .build();
    }
}
