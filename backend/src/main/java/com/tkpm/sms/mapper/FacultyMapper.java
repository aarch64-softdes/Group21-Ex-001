package com.tkpm.sms.mapper;

import com.tkpm.sms.dto.response.FacultyDto;
import com.tkpm.sms.entity.Faculty;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FacultyMapper {

    FacultyDto toFacultyDto(Faculty faculty);
    
    Faculty toFaculty(FacultyDto facultyDto);
}
