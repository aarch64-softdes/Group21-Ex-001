package com.tkpm.sms.infrastructure.mapper;

import com.tkpm.sms.application.dto.request.faculty.FacultyRequestDto;
import com.tkpm.sms.application.dto.response.FacultyDto;
import com.tkpm.sms.application.mapper.FacultyMapper;
import com.tkpm.sms.domain.model.Faculty;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.context.i18n.LocaleContextHolder;

@Mapper(componentModel = "spring", imports = {LocaleContextHolder.class})
public interface FacultyMapperImpl extends FacultyMapper {

    @Override
    @Mapping(target = "name", expression = "java(faculty.getNameByLanguage(LocaleContextHolder.getLocale().getLanguage()))")
    FacultyDto toDto(Faculty faculty);

    @Override
    @Mapping(target = "name", ignore = true)
    Faculty toDomain(FacultyDto facultyDto);

    @Override
    @Mapping(target = "name", ignore = true)
    Faculty toDomain(FacultyRequestDto facultyRequestDto);

    @Override
    @Mapping(target = "name", ignore = true)
    void toDomain(FacultyRequestDto facultyDto, @MappingTarget Faculty faculty);
}
