package com.tkpm.sms.service;

import com.tkpm.sms.dto.request.StudentCreateRequestDto;
import com.tkpm.sms.dto.request.StudentUpdateRequestDto;
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

    public Student addNewStudent(StudentCreateRequestDto studentCreateRequestDto) {
        if (studentRepository.existsStudentByStudentId(studentCreateRequestDto.getStudentId())) {
            var errorCode = ErrorCode.CONFLICT;
            errorCode.setMessage(String.format("Student with id %s already existed", studentCreateRequestDto.getStudentId()));
            throw new ApplicationException(errorCode);
        }
        if (studentRepository.existsStudentByEmail(studentCreateRequestDto.getEmail())) {
            var errorCode = ErrorCode.CONFLICT;
            errorCode.setMessage(String.format("Student with email %s already existed", studentCreateRequestDto.getEmail()));
            throw new ApplicationException(errorCode);
        }
        if (studentRepository.existsStudentByPhone(studentCreateRequestDto.getPhone())) {
            var errorCode = ErrorCode.CONFLICT;
            errorCode.setMessage(String.format("Student with phone number %s already existed", studentCreateRequestDto.getPhone()));
            throw new ApplicationException(errorCode);
        }

        Student student = studentMapper.createStudent(studentCreateRequestDto);

        student.setStatus(Status.valueOf(studentCreateRequestDto.getStatus()));
        student.setFaculty(Faculty.fromString(studentCreateRequestDto.getFaculty()));
        student.setGender(Gender.valueOf(studentCreateRequestDto.getGender()));

        student = studentRepository.save(student);
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

    public Student updateStudent(String id, StudentUpdateRequestDto studentUpdateRequestDto) {
        var student = studentRepository.findById(id)
                .orElseThrow(() -> {
                    var errorCode = ErrorCode.NOT_FOUND;
                    errorCode.setMessage(String.format("Student with id %s not found", id));
                    return new ApplicationException(errorCode);
                });

        if (!student.getEmail().equals(studentUpdateRequestDto.getEmail())
                && studentRepository.existsStudentByEmail(studentUpdateRequestDto.getEmail())) {
            var errorCode = ErrorCode.CONFLICT;
            errorCode.setMessage(String.format("Student with email %s already existed",
                    studentUpdateRequestDto.getEmail()));
            throw new ApplicationException(errorCode);
        }

        if (!student.getPhone().equals(studentUpdateRequestDto.getPhone())
                && studentRepository.existsStudentByPhone(studentUpdateRequestDto.getPhone())) {
            var errorCode = ErrorCode.CONFLICT;
            errorCode.setMessage(String.format("Student with phone number %s already existed",
                    studentUpdateRequestDto.getPhone()));
            throw new ApplicationException(errorCode);
        }

        studentMapper.updateStudent(student, studentUpdateRequestDto);
        student.setStatus(Status.valueOf(studentUpdateRequestDto.getStatus()));
        student.setFaculty(Faculty.fromString(studentUpdateRequestDto.getFaculty()));
        student.setGender(Gender.valueOf(studentUpdateRequestDto.getGender()));

        student = studentRepository.save(student);

        return student;
    }
}
