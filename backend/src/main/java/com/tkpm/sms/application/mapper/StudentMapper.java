package com.tkpm.sms.application.mapper;

import com.tkpm.sms.application.dto.request.student.StudentCreateRequestDto;
import com.tkpm.sms.application.dto.request.student.StudentUpdateRequestDto;
import com.tkpm.sms.application.dto.response.student.StudentDto;
import com.tkpm.sms.application.dto.response.student.StudentFileDto;
import com.tkpm.sms.application.dto.response.student.StudentMinimalDto;
import com.tkpm.sms.domain.model.Student;
import com.tkpm.sms.domain.enums.Gender;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring",
        imports = {Gender.class},
        uses = {PhoneMapper.class, AddressMapper.class, IdentityMapper.class})
public abstract class StudentMapper {

    @Autowired
    protected PhoneMapper phoneMapper;

    @Mapping(target = "faculty", source = "faculty.name")
    @Mapping(target = "program", source = "program.name")
    @Mapping(target = "status", source = "status.name")
    @Mapping(target = "phone", expression = "java(phoneMapper.toPhoneDto(student.getPhone()))")
    @Mapping(target = "gender", source = "gender", qualifiedByName = "genderEnumToString")
    public abstract StudentDto toStudentDto(Student student);

    @Mapping(target = "faculty", source = "faculty.name")
    @Mapping(target = "program", source = "program.name")
    @Mapping(target = "status", source = "status.name")
    @Mapping(target = "phone", expression = "java(phoneMapper.toPhoneDto(student.getPhone()))")
    @Mapping(target = "gender", source = "gender", qualifiedByName = "genderEnumToString")
    public abstract StudentMinimalDto toStudentMinimalDto(Student student);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "faculty", ignore = true)
    @Mapping(target = "program", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "phone", ignore = true)
    @Mapping(target = "permanentAddress", ignore = true)
    @Mapping(target = "temporaryAddress", ignore = true)
    @Mapping(target = "mailingAddress", ignore = true)
    @Mapping(target = "identity", ignore = true)
    @Mapping(target = "gender", source = "gender", qualifiedByName = "stringToGender")
    public abstract Student toStudent(StudentCreateRequestDto requestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "faculty", ignore = true)
    @Mapping(target = "program", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "phone", ignore = true)
    @Mapping(target = "permanentAddress", ignore = true)
    @Mapping(target = "temporaryAddress", ignore = true)
    @Mapping(target = "mailingAddress", ignore = true)
    @Mapping(target = "identity", ignore = true)
    @Mapping(target = "gender", source = "gender", qualifiedByName = "stringToGender")
    public abstract void updateStudentFromDto(StudentUpdateRequestDto requestDto, @MappingTarget Student student);

    @Mapping(target = "permanentAddress", expression = "java(ImportFileUtils.parseAddressCreateRequestDto(studentFileDto.getPermanentAddress()))")
    @Mapping(target = "temporaryAddress", expression = "java(ImportFileUtils.parseAddressCreateRequestDto(studentFileDto.getTemporaryAddress()))")
    @Mapping(target = "mailingAddress", expression = "java(ImportFileUtils.parseAddressCreateRequestDto(studentFileDto.getMailingAddress()))")
    @Mapping(target = "identity", expression = "java(ImportFileUtils.parseIdentityCreateRequestDto(studentFileDto))")
    @Mapping(target = "phone", expression = "java(phoneMapper.toPhoneRequestDto(studentFileDto.getPhone()))")
    public abstract StudentCreateRequestDto toStudentCreateRequest(StudentFileDto studentFileDto);

    @Mapping(target = "permanentAddress", expression = "java(student.getPermanentAddress() != null ? student.getPermanentAddress().getFullAddress() : \"\")")
    @Mapping(target = "temporaryAddress", expression = "java(student.getTemporaryAddress() != null ? student.getTemporaryAddress().getFullAddress() : \"\")")
    @Mapping(target = "mailingAddress", expression = "java(student.getMailingAddress() != null ? student.getMailingAddress().getFullAddress() : \"\")")
    @Mapping(target = "identityType", source = "identity.type.displayName")
    @Mapping(target = "identityNumber", source = "identity.number")
    @Mapping(target = "identityIssuedBy", source = "identity.issuedBy")
    @Mapping(target = "identityIssuedDate", source = "identity.issuedDate")
    @Mapping(target = "identityExpiryDate", source = "identity.expiryDate")
    @Mapping(target = "identityHasChip", source = "identity.hasChip")
    @Mapping(target = "identityNotes", source = "identity.notes")
    @Mapping(target = "identityCountry", source = "identity.country")
    @Mapping(target = "faculty", source = "faculty.name")
    @Mapping(target = "program", source = "program.name")
    @Mapping(target = "status", source = "status.name")
    @Mapping(target = "gender", source = "gender", qualifiedByName = "genderEnumToString")
    public abstract StudentFileDto toStudentFileDto(Student student);

    @Named("stringToGender")
    protected Gender stringToGender(String gender) {
        if (gender == null) {
            return null;
        }
        return Gender.fromDisplayName(gender);
    }

    @Named("genderEnumToString")
    protected String genderEnumToString(Gender gender) {
        return gender != null ? gender.getDisplayName() : null;
    }
}