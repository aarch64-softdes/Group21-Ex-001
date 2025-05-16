package com.tkpm.sms.infrastructure.mapper;

import com.tkpm.sms.application.dto.request.program.ProgramRequestDto;
import com.tkpm.sms.application.dto.response.ProgramDto;
import com.tkpm.sms.application.mapper.ProgramMapper;
import com.tkpm.sms.domain.model.Program;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.context.i18n.LocaleContextHolder;

@Mapper(componentModel = "spring", imports = {LocaleContextHolder.class})
public interface ProgramMapperImpl extends ProgramMapper {
    @Override
    @Mapping(target = "name", expression = "java(program.getNameByLanguage(LocaleContextHolder.getLocale().getLanguage()))")
    ProgramDto toDto(Program program);

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "students", ignore = true)
    @Mapping(target = "name", ignore = true)
    Program toDomain(ProgramRequestDto programRequestDto);

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "students", ignore = true)
    @Mapping(target = "name", ignore = true)
    void toDomain(ProgramRequestDto programRequestDto, @MappingTarget Program program);
}
