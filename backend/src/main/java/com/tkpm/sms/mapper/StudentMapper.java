package com.tkpm.sms.mapper;

import com.tkpm.sms.dto.request.StudentCreateRequestDto;
import com.tkpm.sms.dto.request.StudentFileImportDto;
import com.tkpm.sms.dto.request.StudentUpdateRequestDto;
import com.tkpm.sms.dto.response.student.StudentDto;
import com.tkpm.sms.dto.response.student.StudentFileExportDto;
import com.tkpm.sms.dto.response.student.StudentMinimalDto;
import com.tkpm.sms.entity.Student;
import com.tkpm.sms.enums.Faculty;
import com.tkpm.sms.enums.Gender;
import com.tkpm.sms.enums.Status;
import com.tkpm.sms.utils.ImportFileUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.Objects;

@Mapper(componentModel = "spring", imports = {ImportFileUtils.class, Gender.class, Faculty.class, Status.class, Objects.class})
public interface StudentMapper {
    StudentDto toStudentDto(Student student);

    StudentMinimalDto toStudentMinimalDto(Student student);

    Student toStudentCreateRequest(StudentDto studentDto);

    @Mapping(target = "permanentAddress", expression = "java(ImportFileUtils.parseAddressCreateRequestDto(studentFileImportDto.getPermanentAddress()))")
    @Mapping(target = "temporaryAddress", expression = "java(ImportFileUtils.parseAddressCreateRequestDto(studentFileImportDto.getTemporaryAddress()))")
    @Mapping(target = "mailingAddress", expression = "java(ImportFileUtils.parseAddressCreateRequestDto(studentFileImportDto.getMailingAddress()))")
    @Mapping(target = "identity",
            expression = "java(ImportFileUtils.parseIdentityCreateRequestDto(" +
                    "studentFileImportDto.getIdentityType(), " +
                    "studentFileImportDto.getIdentityNumber(), " +
                    "studentFileImportDto.getIdentityIssuedBy(), " +
                    "studentFileImportDto.getIdentityIssuedDate(), " +
                    "studentFileImportDto.getIdentityExpiryDate()))"
    )
    StudentCreateRequestDto toStudentCreateRequest(StudentFileImportDto studentFileImportDto);

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

    @Mapping(
            target = "permanentAddress",
            expression = "java(Objects.isNull(student.getPermanentAddress()) ? \"\" : student.getPermanentAddress().toString())"
    )
    @Mapping(
            target = "temporaryAddress",
            expression = "java(Objects.isNull(student.getTemporaryAddress()) ? \"\" : student.getTemporaryAddress().toString())"
    )
    @Mapping(
            target = "mailingAddress",
            expression = "java(Objects.isNull(student.getMailingAddress()) ? \"\" : student.getMailingAddress().toString())"
    )
    @Mapping(target = "identityType", source = "identity.type")
    @Mapping(target = "identityNumber", source = "identity.number")
    @Mapping(target = "identityIssuedBy", source = "identity.issuedBy")
    @Mapping(target = "identityIssuedDate", source = "identity.issuedDate")
    @Mapping(target = "identityExpiryDate", source = "identity.expiryDate")
    StudentFileExportDto toStudentFileExportDto(Student student);
}
