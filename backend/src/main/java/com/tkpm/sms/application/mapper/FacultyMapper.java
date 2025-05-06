package com.tkpm.sms.application.mapper;

import com.tkpm.sms.application.dto.request.faculty.FacultyRequestDto;
import com.tkpm.sms.application.dto.response.FacultyDto;
import com.tkpm.sms.domain.model.Faculty;
import org.mapstruct.MappingTarget;

import java.util.List;

public interface FacultyMapper {
    FacultyDto toDto(Faculty faculty);

    Faculty toDomain(FacultyDto facultyDto);

    Faculty toDomain(FacultyRequestDto facultyRequestDto);

    void toDomain(FacultyRequestDto facultyDto, @MappingTarget Faculty faculty);

    List<FacultyDto> toDtos(List<Faculty> faculties);

    List<Faculty> toDomainModels(List<FacultyDto> facultyDtos);
}
