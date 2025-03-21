package com.tkpm.sms.mapper;

import com.tkpm.sms.dto.request.StudentCreateRequestDto;
import com.tkpm.sms.entity.Faculty;
import com.tkpm.sms.entity.Program;
import com.tkpm.sms.entity.Status;
import com.tkpm.sms.dto.request.StudentUpdateRequestDto;
import com.tkpm.sms.dto.response.student.StudentDto;
import com.tkpm.sms.dto.response.student.StudentMinimalDto;
import com.tkpm.sms.entity.Student;
import com.tkpm.sms.service.FacultyService;
import com.tkpm.sms.service.ProgramService;
import com.tkpm.sms.service.StatusService;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
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
    @Mapping(target = "gender", qualifiedByName = "toGender")
    Student toStudent(StudentDto studentDto,
                      @Context FacultyService facultyService,
                      @Context ProgramService programService,
                      @Context StatusService statusService);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", qualifiedByName = "toStatus")
    @Mapping(target = "program", qualifiedByName = "toProgram")
    @Mapping(target = "faculty", qualifiedByName = "toFaculty")
    @Mapping(target = "gender", qualifiedByName = "toGender")
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
}
