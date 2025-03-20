package com.tkpm.sms.mapper;

import com.tkpm.sms.dto.request.StudentCreateRequestDto;
import com.tkpm.sms.dto.request.StudentUpdateRequestDto;
import com.tkpm.sms.dto.response.student.StudentDto;
import com.tkpm.sms.dto.response.student.StudentFileDto;
import com.tkpm.sms.dto.response.student.StudentMinimalDto;
import com.tkpm.sms.entity.Program;
import com.tkpm.sms.entity.Student;
import com.tkpm.sms.entity.Faculty;
import com.tkpm.sms.entity.Status;
import com.tkpm.sms.enums.Gender;
import com.tkpm.sms.utils.ImportFileUtils;
import com.tkpm.sms.service.FacultyService;
import com.tkpm.sms.service.ProgramService;
import com.tkpm.sms.service.StatusService;
import org.mapstruct.*;

import java.util.Objects;

@Mapper(componentModel = "spring", imports = {ImportFileUtils.class, Gender.class, Faculty.class, Status.class, Objects.class})
public interface StudentMapper {

    @Mapping(target = "status", source = "status.name")
    @Mapping(target = "program", source = "program.name")
    @Mapping(target = "faculty", source = "faculty.name")
    StudentDto toStudentDto(Student student);

    @Mapping(target = "status", source = "status.name")
    @Mapping(target = "program", source = "program.name")
    @Mapping(target = "faculty", source = "faculty.name")

    StudentMinimalDto toStudentMinimalDto(Student student);


    @Mapping(target = "status", qualifiedByName = "toStatus")
    @Mapping(target = "program", qualifiedByName = "toProgram")
    @Mapping(target = "faculty", qualifiedByName = "toFaculty")
    Student toStudentCreateRequest(StudentDto studentDto,
                      @Context FacultyService facultyService,
                      @Context ProgramService programService,
                      @Context StatusService statusService);

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
    StudentCreateRequestDto toStudentCreateRequest(StudentFileDto studentFileImportDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", qualifiedByName = "toStatus")
    @Mapping(target = "program", qualifiedByName = "toProgram")
    @Mapping(target = "faculty", qualifiedByName = "toFaculty")
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
    @Mapping(target = "faculty", source = "faculty.name")
    @Mapping(target = "program", source = "program.name")
    @Mapping(target = "status", source = "status.name")
    StudentFileDto toStudentFileDto(Student student);
}
