package com.tkpm.sms.mapper;

import com.tkpm.sms.dto.request.StudentUpdateRequestDto;
import com.tkpm.sms.dto.response.StudentDto;
import com.tkpm.sms.dto.request.StudentCreateRequestDto;
import com.tkpm.sms.entity.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface StudentMapper {
    Student toStudent(StudentDto studentDto);
    StudentDto toStudentDto(Student student);

    @Mapping(target = "status", ignore = true)
    @Mapping(target = "faculty", ignore = true)
    @Mapping(target = "gender", ignore = true)
    void updateStudent(@MappingTarget Student student, StudentUpdateRequestDto request);

    @Mapping(target = "status", ignore = true)
    @Mapping(target = "faculty", ignore = true)
    @Mapping(target = "gender", ignore = true)
    Student createStudent(StudentCreateRequestDto request);
}
