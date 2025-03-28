package com.tkpm.sms.mapper;

import java.util.Objects;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.tkpm.sms.dto.request.phone.PhoneRequestDto;
import com.tkpm.sms.dto.response.PhoneDto;
import com.tkpm.sms.utils.PhoneUtils;
import org.mapstruct.Context;
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
import com.tkpm.sms.entity.Faculty;
import com.tkpm.sms.entity.Program;
import com.tkpm.sms.entity.Status;
import com.tkpm.sms.entity.Student;
import com.tkpm.sms.enums.Gender;
import com.tkpm.sms.exceptions.ApplicationException;
import com.tkpm.sms.exceptions.ErrorCode;
import com.tkpm.sms.service.FacultyService;
import com.tkpm.sms.service.ProgramService;
import com.tkpm.sms.service.StatusService;
import com.tkpm.sms.utils.ImportFileUtils;

@Mapper(componentModel = "spring",
        imports = {ImportFileUtils.class, Gender.class,
                Faculty.class, Status.class, Objects.class, PhoneUtils.class})
public interface StudentMapper {

    @Mapping(target = "status", source = "status.name")
    @Mapping(target = "program", source = "program.name")
    @Mapping(target = "faculty", source = "faculty.name")
    @Mapping(target = "phone", expression = "java(PhoneUtils.parsePhoneToPhoneDto(student.getPhone()))")
    StudentDto toStudentDto(Student student);

    @Mapping(target = "status", source = "status.name")
    @Mapping(target = "program", source = "program.name")
    @Mapping(target = "faculty", source = "faculty.name")
    @Mapping(target = "phone", expression = "java(PhoneUtils.parsePhoneToPhoneDto(student.getPhone()))")
    StudentMinimalDto toStudentMinimalDto(Student student);


    @Mapping(target = "status", qualifiedByName = "toStatus")
    @Mapping(target = "program", qualifiedByName = "toProgram")
    @Mapping(target = "faculty", qualifiedByName = "toFaculty")
    @Mapping(target = "gender", qualifiedByName = "toGender")
    @Mapping(target = "phone", ignore = true)
    Student toStudent(StudentDto studentDto,
                      @Context FacultyService facultyService,
                      @Context ProgramService programService,
                      @Context StatusService statusService);

    @Mapping(target = "permanentAddress", expression = "java(ImportFileUtils.parseAddressCreateRequestDto(studentFileImportDto.getPermanentAddress()))")
    @Mapping(target = "temporaryAddress", expression = "java(ImportFileUtils.parseAddressCreateRequestDto(studentFileImportDto.getTemporaryAddress()))")
    @Mapping(target = "mailingAddress", expression = "java(ImportFileUtils.parseAddressCreateRequestDto(studentFileImportDto.getMailingAddress()))")
    @Mapping(target = "identity", expression = "java(ImportFileUtils.parseIdentityCreateRequestDto(studentFileImportDto))")
    @Mapping(target = "phone", expression = "java(PhoneUtils.parsePhoneToPhoneRequestDto(studentFileImportDto.getPhone()))")
    StudentCreateRequestDto toStudentCreateRequest(StudentFileDto studentFileImportDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", qualifiedByName = "toStatus")
    @Mapping(target = "program", qualifiedByName = "toProgram")
    @Mapping(target = "faculty", qualifiedByName = "toFaculty")
    @Mapping(target = "gender", qualifiedByName = "toGender")
    @Mapping(target = "phone", ignore = true)
    @Mapping(target = "mailingAddress", ignore = true)
    @Mapping(target = "temporaryAddress", ignore = true)
    @Mapping(target = "permanentAddress", ignore = true)
    void updateStudent(@MappingTarget Student student,
                       StudentUpdateRequestDto request,
                       @Context FacultyService facultyService,
                       @Context ProgramService programService,
                       @Context StatusService statusService);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", qualifiedByName = "toStatus")
    @Mapping(target = "program", qualifiedByName = "toProgram")
    @Mapping(target = "faculty", qualifiedByName = "toFaculty")
    @Mapping(target = "gender", qualifiedByName = "toGender")
    @Mapping(target = "phone", ignore = true)
    @Mapping(target = "mailingAddress", ignore = true)
    @Mapping(target = "temporaryAddress", ignore = true)
    @Mapping(target = "permanentAddress", ignore = true)
    Student createStudent(StudentCreateRequestDto request,
                          @Context FacultyService facultyService,
                          @Context ProgramService programService,
                          @Context StatusService statusService);

    @Named("toFaculty")
    default Faculty toFaculty(String name, @Context FacultyService facultyService) {
        return facultyService.getFacultyByName(name);
    }

    @Named("toStatus")
    default Status toStatus(String name, @Context StatusService statusService) {
        return statusService.getStatusByName(name);
    }

    @Named("toProgram")
    default Program toProgram(String name, @Context ProgramService programService) {
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
    StudentFileDto toStudentFileDto(Student student);

    default String formatAddress(Address address) {
        return Objects.isNull(address) ? "" : address.toString();
    }

    @Named("toGender")
    default Gender toGender(String gender){
        if(gender.equalsIgnoreCase("male")){
            return Gender.Male;
        } else if (gender.equalsIgnoreCase("female")) {
            return Gender.Female;
        }else{
            throw new ApplicationException(ErrorCode.UNCATEGORIZED.withMessage("Gender not supported"));
        }
    }

}
