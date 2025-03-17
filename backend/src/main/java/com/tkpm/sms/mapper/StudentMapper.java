package com.tkpm.sms.mapper;

import com.tkpm.sms.dto.reponse.StudentDto;
import com.tkpm.sms.dto.request.StudentRequest;
import com.tkpm.sms.entity.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface StudentMapper {
    Student toStudent(StudentDto studentDto);
    StudentDto toStudentDto(Student student);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "studentId", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "faculty", ignore = true)
    @Mapping(target = "gender", ignore = true)
    void updateStudent(@MappingTarget Student student, StudentRequest request);

    @Mapping(target = "status", ignore = true)
    @Mapping(target = "faculty", ignore = true)
    @Mapping(target = "gender", ignore = true)
    Student createStudent(StudentRequest request);
}
