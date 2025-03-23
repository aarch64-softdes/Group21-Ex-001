package com.tkpm.sms.mapper;

import com.tkpm.sms.dto.response.ProgramDto;
import com.tkpm.sms.entity.Program;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProgramMapper {
    ProgramDto toProgramDto(Program program);
    
    Program toProgram(ProgramDto programDto);
}
