package com.tkpm.sms.infrastructure.mapper;

import com.tkpm.sms.application.dto.request.program.ProgramRequestDto;
import com.tkpm.sms.application.mapper.ProgramMapper;
import com.tkpm.sms.domain.model.Program;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProgramMapperImpl extends ProgramMapper {
    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "students", ignore = true)
    Program toEntity(ProgramRequestDto programRequestDto);

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "students", ignore = true)
    void updateProgramFromDto(ProgramRequestDto programRequestDto, @MappingTarget Program program);
}
