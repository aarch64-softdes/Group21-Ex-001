package com.tkpm.sms.application.service.interfaces;

import com.tkpm.sms.application.dto.request.student.StudentCollectionRequest;
import com.tkpm.sms.application.dto.request.student.StudentCreateRequestDto;
import com.tkpm.sms.application.dto.request.student.StudentUpdateRequestDto;
import com.tkpm.sms.application.dto.response.student.StudentFileDto;
import com.tkpm.sms.domain.common.PageResponse;
import com.tkpm.sms.domain.model.Student;

import java.util.List;

public interface StudentService {
    PageResponse<Student> findAll(StudentCollectionRequest search);
    Student getStudentDetail(String id);
    Student createStudent(StudentCreateRequestDto studentCreateRequestDto);
    Student updateStudent(String id, StudentUpdateRequestDto updateRequestDto);
    void deleteStudentById(String id);
    void saveListStudentFromFile(List<StudentFileDto> students);
}