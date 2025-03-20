package com.tkpm.sms.mapper;

import com.tkpm.sms.dto.request.StudentCreateRequestDto;
import com.tkpm.sms.dto.request.StudentUpdateRequestDto;
import com.tkpm.sms.dto.response.student.StudentDto;
import com.tkpm.sms.dto.response.student.StudentFileExportDto;
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

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "faculty", ignore = true)
    @Mapping(target = "gender", ignore = true)
    @Mapping(target = "mailingAddress", ignore = true)
    @Mapping(target = "temporaryAddress", ignore = true)
    @Mapping(target = "permanentAddress", ignore = true)
    void updateStudent(@MappingTarget Student student, StudentUpdateRequestDto request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "faculty", ignore = true)
    @Mapping(target = "gender", ignore = true)
    @Mapping(target = "mailingAddress", ignore = true)
    @Mapping(target = "temporaryAddress", ignore = true)
    @Mapping(target = "permanentAddress", ignore = true)
    Student createStudent(StudentCreateRequestDto request);

    @Mapping(target = "permanentStreet", source = "permanentAddress.street", defaultValue = "")
    @Mapping(target = "permanentWard", source = "permanentAddress.ward", defaultValue = "")
    @Mapping(target = "permanentDistrict", source = "permanentAddress.district", defaultValue = "")
    @Mapping(target = "permanentCountry", source = "permanentAddress.country", defaultValue = "")
    @Mapping(target = "temporaryStreet", source = "temporaryAddress.street", defaultValue = "")
    @Mapping(target = "temporaryWard", source = "temporaryAddress.ward", defaultValue = "")
    @Mapping(target = "temporaryDistrict", source = "temporaryAddress.district", defaultValue = "")
    @Mapping(target = "temporaryCountry", source = "temporaryAddress.country", defaultValue = "")
    @Mapping(target = "mailingStreet", source = "mailingAddress.street", defaultValue = "")
    @Mapping(target = "mailingWard", source = "mailingAddress.ward", defaultValue = "")
    @Mapping(target = "mailingDistrict", source = "mailingAddress.district", defaultValue = "")
    @Mapping(target = "mailingCountry", source = "mailingAddress.country", defaultValue = "")
    StudentFileExportDto toStudentFileExportDto(Student student);
}
