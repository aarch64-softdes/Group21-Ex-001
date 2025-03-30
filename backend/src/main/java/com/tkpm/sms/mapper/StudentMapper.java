package com.tkpm.sms.mapper;

import java.util.Objects;

import com.tkpm.sms.application.service.interfaces.FacultyService;
import com.tkpm.sms.domain.model.Faculty;
import com.tkpm.sms.infrastructure.persistence.entity.FacultyEntity;
import com.tkpm.sms.service.AddressService;
import com.tkpm.sms.utils.PhoneParser;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import com.tkpm.sms.dto.request.student.StudentCreateRequestDto;
import com.tkpm.sms.dto.request.student.StudentUpdateRequestDto;
import com.tkpm.sms.dto.response.student.StudentDto;
import com.tkpm.sms.dto.response.student.StudentFileDto;
import com.tkpm.sms.dto.response.student.StudentMinimalDto;
import com.tkpm.sms.entity.Address;
import com.tkpm.sms.entity.Program;
import com.tkpm.sms.entity.Status;
import com.tkpm.sms.entity.Student;
import com.tkpm.sms.enums.Gender;
import com.tkpm.sms.exceptions.ApplicationException;
import com.tkpm.sms.exceptions.ErrorCode;
import com.tkpm.sms.service.ProgramService;
import com.tkpm.sms.service.StatusService;
import com.tkpm.sms.utils.ImportFileUtils;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring",
        imports = {ImportFileUtils.class, Gender.class,
                Faculty.class, Status.class, Objects.class, PhoneParser.class})
@RequiredArgsConstructor
public abstract class StudentMapper {
    @Autowired
    protected PhoneParser phoneParser;
    @Autowired
    protected FacultyService facultyService;
    @Autowired
    protected StatusService statusService;
    @Autowired
    protected AddressService addressService;
    @Autowired
    protected ProgramService programService;

    @Mapping(target = "status", source = "status.name")
    @Mapping(target = "program", source = "program.name")
    @Mapping(target = "faculty", source = "faculty.name")
    @Mapping(target = "phone", expression = "java(phoneParser.parsePhoneToPhoneDto(student.getPhone()))")
    public abstract StudentDto toDto(Student student);

    @Mapping(target = "status", source = "status.name")
    @Mapping(target = "program", source = "program.name")
    @Mapping(target = "faculty", source = "faculty.name")
    @Mapping(target = "phone", expression = "java(phoneParser.parsePhoneToPhoneDto(student.getPhone()))")
    public abstract StudentMinimalDto toMinimalDto(Student student);


    @Mapping(target = "status", qualifiedByName = "toStatus")
    @Mapping(target = "program", qualifiedByName = "toProgram")
    @Mapping(target = "faculty", qualifiedByName = "toFaculty")
    @Mapping(target = "gender", qualifiedByName = "toGender")
    @Mapping(target = "phone", ignore = true)
    public abstract Student toStudent(StudentDto studentDto);

    @Mapping(target = "permanentAddress", expression = "java(ImportFileUtils.parseAddressCreateRequestDto(studentFileImportDto.getPermanentAddress()))")
    @Mapping(target = "temporaryAddress", expression = "java(ImportFileUtils.parseAddressCreateRequestDto(studentFileImportDto.getTemporaryAddress()))")
    @Mapping(target = "mailingAddress", expression = "java(ImportFileUtils.parseAddressCreateRequestDto(studentFileImportDto.getMailingAddress()))")
    @Mapping(target = "identity", expression = "java(ImportFileUtils.parseIdentityCreateRequestDto(studentFileImportDto))")
    @Mapping(target = "phone", expression = "java(phoneParser.parsePhoneToPhoneRequestDto(studentFileImportDto.getPhone()))")
    public abstract StudentCreateRequestDto toStudentCreateRequest(StudentFileDto studentFileImportDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", qualifiedByName = "toStatus")
    @Mapping(target = "program", qualifiedByName = "toProgram")
    @Mapping(target = "faculty", qualifiedByName = "toFaculty")
    @Mapping(target = "gender", qualifiedByName = "toGender")
    @Mapping(target = "phone", ignore = true)
    @Mapping(target = "mailingAddress", ignore = true)
    @Mapping(target = "temporaryAddress", ignore = true)
    @Mapping(target = "permanentAddress", ignore = true)
    public abstract void updateStudent(@MappingTarget Student student,
                       StudentUpdateRequestDto request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", qualifiedByName = "toStatus")
    @Mapping(target = "program", qualifiedByName = "toProgram")
    @Mapping(target = "faculty", qualifiedByName = "toFaculty")
    @Mapping(target = "gender", qualifiedByName = "toGender")
    @Mapping(target = "phone", ignore = true)
    @Mapping(target = "mailingAddress", ignore = true)
    @Mapping(target = "temporaryAddress", ignore = true)
    @Mapping(target = "permanentAddress", ignore = true)
    public abstract Student toEntity(StudentCreateRequestDto request);

    @Named("toFaculty")
    protected FacultyEntity toFaculty(String name) {
        return new FacultyEntity();
    }

    @Named("toStatus")
    protected Status toStatus(String name) {
        return statusService.getStatusByName(name);
    }

    @Named("toProgram")
    protected Program toProgram(String name) {
        return programService.getProgramByName(name);
    }

    @Mapping(target = "permanentAddress", expression = "java(formatAddress(student.getPermanentAddress()))")
    @Mapping(target = "temporaryAddress", expression = "java(formatAddress(student.getTemporaryAddress()))")
    @Mapping(target = "mailingAddress", expression = "java(formatAddress(student.getMailingAddress()))")
    @Mapping(target = "identityType", source = "identity.type")
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
    public abstract StudentFileDto toStudentFileDto(Student student);

    protected String formatAddress(Address address) {
        return Objects.isNull(address) ? "" : address.toString();
    }

    @Named("toGender")
    protected Gender toGender(String gender){
        if(gender.equalsIgnoreCase("male")){
            return Gender.Male;
        } else if (gender.equalsIgnoreCase("female")) {
            return Gender.Female;
        }else{
            throw new ApplicationException(ErrorCode.UNCATEGORIZED.withMessage("Gender not supported"));
        }
    }

}