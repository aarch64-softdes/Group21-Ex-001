package com.tkpm.sms.mapper;

import com.tkpm.sms.dto.request.StudentCreateRequestDto;
import com.tkpm.sms.dto.request.StudentUpdateRequestDto;
import com.tkpm.sms.dto.response.student.StudentDto;
import com.tkpm.sms.dto.response.student.StudentMinimalDto;
import com.tkpm.sms.entity.Student;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface StudentMapper {
    @Mapping(target = "citizenship", source = "citizenship.countryName")
    StudentDto toStudentDto(Student student);

    @Mapping(target = "citizenship", source = "citizenship.countryName")
    StudentMinimalDto toStudentMinimalDto(Student student);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "faculty", ignore = true)
    @Mapping(target = "gender", ignore = true)
    @Mapping(target = "mailingAddress", ignore = true)
    @Mapping(target = "temporaryAddress", ignore = true)
    @Mapping(target = "permanentAddress", ignore = true)
    @Mapping(target = "citizenship", ignore = true)
    void updateStudent(@MappingTarget Student student, StudentUpdateRequestDto request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "faculty", ignore = true)
    @Mapping(target = "gender", ignore = true)
    @Mapping(target = "mailingAddress", ignore = true)
    @Mapping(target = "temporaryAddress", ignore = true)
    @Mapping(target = "permanentAddress", ignore = true)
    @Mapping(target = "citizenship", ignore = true)
    Student createStudent(StudentCreateRequestDto request);
}
