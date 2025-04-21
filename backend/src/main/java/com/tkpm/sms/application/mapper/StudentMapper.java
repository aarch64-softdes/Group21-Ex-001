package com.tkpm.sms.application.mapper;

import com.tkpm.sms.application.dto.request.student.StudentCreateRequestDto;
import com.tkpm.sms.application.dto.request.student.StudentUpdateRequestDto;
import com.tkpm.sms.application.dto.response.student.StudentDto;
import com.tkpm.sms.application.dto.response.student.StudentFileDto;
import com.tkpm.sms.application.dto.response.student.StudentMinimalDto;
import com.tkpm.sms.domain.model.Student;

public interface StudentMapper {
    StudentDto toStudentDto(Student student);

    Student toStudent(StudentCreateRequestDto requestDto);

    void updateStudentFromDto(StudentUpdateRequestDto requestDto, Student student);

    StudentCreateRequestDto toStudentCreateRequest(StudentFileDto studentFileDto);

    StudentFileDto toStudentFileDto(Student student);

    StudentMinimalDto toStudentMinimalDto(Student student);
}
