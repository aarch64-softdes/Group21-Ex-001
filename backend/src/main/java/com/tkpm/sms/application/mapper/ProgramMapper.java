package com.tkpm.sms.application.mapper;

import com.tkpm.sms.application.dto.request.program.ProgramRequestDto;
import com.tkpm.sms.application.dto.response.ProgramDto;
import com.tkpm.sms.domain.model.Program;

public interface ProgramMapper {
    ProgramDto toDto(Program program);

    Program toDomain(ProgramRequestDto programRequestDto);

    void toDomain(ProgramRequestDto programRequestDto, Program program);
}