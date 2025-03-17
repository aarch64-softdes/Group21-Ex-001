package com.tkpm.sms.service;

import com.tkpm.sms.dto.request.StudentRequest;
import com.tkpm.sms.entity.Student;
import com.tkpm.sms.enums.Faculty;
import com.tkpm.sms.enums.Gender;
import com.tkpm.sms.enums.Status;
import com.tkpm.sms.exceptions.ApplicationException;
import com.tkpm.sms.exceptions.ErrorCode;
import com.tkpm.sms.mapper.StudentMapper;
import com.tkpm.sms.repository.StudentRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StudentService {
    StudentRepository studentRepository;
    int PAGE_SIZE = 5;
    StudentMapper studentMapper;

    public Page<Student> getStudents(int page, String sortName, String sortType, String search) {
        Pageable pageable = PageRequest.of(page - 1, PAGE_SIZE,
                Sort.by(sortType.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC,
                        sortName));

        return studentRepository.getStudents(search, pageable);
    }

    public Student getStudentDetail(String id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> {
                    var errorCode = ErrorCode.NOT_FOUND;
                    errorCode.setMessage(String.format("Student with id %s not found", id));
                    return new ApplicationException(errorCode);
                });
    }

    public Student addNewStudent(StudentRequest studentRequest) {
        if (studentRepository.existsStudentByStudentId(studentRequest.getStudentId())) {
            var errorCode = ErrorCode.CONFLICT;
            errorCode.setMessage(String.format("Student with id %s already existed", studentRequest.getStudentId()));
            throw new ApplicationException(errorCode);
        }
        if (studentRepository.existsStudentByEmail(studentRequest.getEmail())) {
            var errorCode = ErrorCode.CONFLICT;
            errorCode.setMessage(String.format("Student with email %s already existed", studentRequest.getEmail()));
            throw new ApplicationException(errorCode);
        }

        Student student = studentMapper.createStudent(studentRequest);

        student.setStatus(Status.valueOf(studentRequest.getStatus()));
        student.setFaculty(Faculty.fromString(studentRequest.getFaculty()));
        student.setGender(Gender.valueOf(studentRequest.getGender()));

        studentRepository.save(student);
        return student;
    }

    public void deleteStudentById(String id) {
        if (studentRepository.existsStudentByStudentId(id)) {
            var errorCode = ErrorCode.NOT_FOUND;
            errorCode.setMessage(String.format("Student with id %s not found", id));
            throw new ApplicationException(errorCode);
        }
        studentRepository.deleteById(id);
    }

    public Student updateStudent(String id, StudentRequest studentRequest) {
        var student = studentRepository.findById(id)
                .orElseThrow(() -> {
                    var errorCode = ErrorCode.NOT_FOUND;
                    errorCode.setMessage(String.format("Student with id %s not found", id));
                    return new ApplicationException(errorCode);
                });

        studentMapper.updateStudent(student, studentRequest);
        student.setStatus(Status.valueOf(studentRequest.getStatus()));
        student.setFaculty(Faculty.valueOf(studentRequest.getFaculty()));
        student.setGender(Gender.valueOf(studentRequest.getGender()));

        studentRepository.save(student);

        return student;
    }
}
