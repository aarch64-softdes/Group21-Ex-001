package com.tkpm.sms.mapper;

import com.tkpm.sms.dto.request.StudentCreateRequestDto;
import com.tkpm.sms.dto.request.StudentUpdateRequestDto;
import com.tkpm.sms.dto.response.student.StudentDto;
import com.tkpm.sms.dto.response.student.StudentMinimalDto;
import com.tkpm.sms.entity.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface StudentMapper {
    StudentDto toStudentDto(Student student);
    StudentMinimalDto toStudentMinimalDto(Student student);
    Student toStudent(StudentDto studentDto);

    @Mapping(target = "status", ignore = true)
    @Mapping(target = "faculty", ignore = true)
    @Mapping(target = "gender", ignore = true)
    @Mapping(target = "mailingAddress", ignore = true)
    @Mapping(target = "temporaryAddress", ignore = true)
    @Mapping(target = "permanentAddress", ignore = true)
    void updateStudent(@MappingTarget Student student, StudentUpdateRequestDto request);

    @Mapping(target = "status", ignore = true)
    @Mapping(target = "faculty", ignore = true)
    @Mapping(target = "gender", ignore = true)
    Student createStudent(StudentCreateRequestDto request);
}
