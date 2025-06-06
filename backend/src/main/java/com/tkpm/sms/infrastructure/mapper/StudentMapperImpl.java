package com.tkpm.sms.infrastructure.mapper;

import com.tkpm.sms.application.dto.request.student.StudentCreateRequestDto;
import com.tkpm.sms.application.dto.request.student.StudentUpdateRequestDto;
import com.tkpm.sms.application.dto.response.student.StudentDto;
import com.tkpm.sms.application.dto.response.student.StudentFileDto;
import com.tkpm.sms.application.dto.response.student.StudentMinimalDto;
import com.tkpm.sms.application.mapper.*;
import com.tkpm.sms.domain.enums.Gender;
import com.tkpm.sms.domain.model.Student;
import com.tkpm.sms.infrastructure.utils.ImportFileUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;

@Mapper(componentModel = "spring", imports = {Gender.class, ImportFileUtils.class,
        LocaleContextHolder.class}, uses = {PhoneMapper.class, AddressMapper.class,
                IdentityMapper.class, FacultyMapper.class, StatusMapper.class, ProgramMapper.class})
public abstract class StudentMapperImpl implements StudentMapper {

    @Autowired
    protected PhoneMapper phoneMapper;

    @Override
    @Mapping(target = "phone", expression = "java(phoneMapper.toPhoneDto(student.getPhone()))")
    @Mapping(target = "gender", source = "gender", qualifiedByName = "genderEnumToString")
    public abstract StudentDto toStudentDto(Student student);

    @Override
    @Mapping(target = "faculty", expression = "java(student.getFaculty().getNameByLanguage(LocaleContextHolder.getLocale().getLanguage()))")
    @Mapping(target = "program", expression = "java(student.getProgram().getNameByLanguage(LocaleContextHolder.getLocale().getLanguage()))")
    @Mapping(target = "status", expression = "java(student.getStatus().getNameByLanguage(LocaleContextHolder.getLocale().getLanguage()))")
    @Mapping(target = "phone", expression = "java(phoneMapper.toPhoneDto(student.getPhone()))")
    @Mapping(target = "gender", source = "gender", qualifiedByName = "genderEnumToString")
    public abstract StudentMinimalDto toStudentMinimalDto(Student student);

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "phone", ignore = true)
    @Mapping(target = "permanentAddress", ignore = true)
    @Mapping(target = "temporaryAddress", ignore = true)
    @Mapping(target = "mailingAddress", ignore = true)
    @Mapping(target = "identity", ignore = true)
    @Mapping(target = "gender", source = "gender", qualifiedByName = "stringToGender")
    public abstract Student toStudent(StudentCreateRequestDto requestDto);

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "phone", ignore = true)
    @Mapping(target = "permanentAddress", ignore = true)
    @Mapping(target = "temporaryAddress", ignore = true)
    @Mapping(target = "mailingAddress", ignore = true)
    @Mapping(target = "identity", ignore = true)
    @Mapping(target = "gender", source = "gender", qualifiedByName = "stringToGender")
    public abstract void updateStudentFromDto(StudentUpdateRequestDto requestDto,
            @MappingTarget Student student);

    @Override
    @Mapping(target = "permanentAddress", expression = "java(ImportFileUtils.parseAddressCreateRequestDto(studentFileDto.getPermanentAddress()))")
    @Mapping(target = "temporaryAddress", expression = "java(ImportFileUtils.parseAddressCreateRequestDto(studentFileDto.getTemporaryAddress()))")
    @Mapping(target = "mailingAddress", expression = "java(ImportFileUtils.parseAddressCreateRequestDto(studentFileDto.getMailingAddress()))")
    @Mapping(target = "identity", expression = "java(ImportFileUtils.parseIdentityCreateRequestDto(studentFileDto))")
    @Mapping(target = "phone", expression = "java(phoneMapper.toPhoneRequestDto(studentFileDto.getPhone()))")
    @Mapping(target = "facultyId", ignore = true)
    @Mapping(target = "programId", ignore = true)
    @Mapping(target = "statusId", ignore = true)
    public abstract StudentCreateRequestDto toStudentCreateRequest(StudentFileDto studentFileDto);

    @Override
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
    @Mapping(target = "faculty", expression = "java(student.getFaculty().getNameByLanguage(LocaleContextHolder.getLocale().getLanguage()))")
    @Mapping(target = "program", expression = "java(student.getProgram().getNameByLanguage(LocaleContextHolder.getLocale().getLanguage()))")
    @Mapping(target = "status", expression = "java(student.getStatus().getNameByLanguage(LocaleContextHolder.getLocale().getLanguage()))")
    @Mapping(target = "phone", expression = "java(student.getPhone().toString())")
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