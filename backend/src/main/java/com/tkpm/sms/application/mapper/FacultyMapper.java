package com.tkpm.sms.application.mapper;

import com.tkpm.sms.application.dto.response.FacultyDto;
import com.tkpm.sms.domain.model.Faculty;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FacultyMapper {
     FacultyDto toDto(Faculty faculty);
     Faculty toDomainModel(FacultyDto facultyDto);
     List<FacultyDto> toDtos(List<Faculty> faculties);
     List<Faculty> toDomainModels(List<FacultyDto> facultyDtos);
}
