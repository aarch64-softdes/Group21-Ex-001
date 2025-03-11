package com.tkpm.sms.service;

import com.tkpm.sms.entity.Student;
import com.tkpm.sms.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public void addNewStudent(Student student) {
        studentRepository.save(student);
    }

    public void deleteStudentById(String id) {
        studentRepository.deleteById(id);
    }

    public void updateStudent(String id, Student student) {
        student.setId(id);
        studentRepository.save(student);
    }

    public List<Student> getStudentsByNameOrId(String find) {
        return studentRepository.getStudentsByNameOrId(find);
    }
}
