package com.tkpm.sms.application.service.interfaces;

import com.tkpm.sms.application.dto.request.common.BaseCollectionRequest;
import com.tkpm.sms.application.dto.request.program.ProgramRequestDto;
import com.tkpm.sms.domain.common.PageResponse;
import com.tkpm.sms.domain.model.Program;

public interface ProgramService {
    PageResponse<Program> getAllPrograms(BaseCollectionRequest search);
    Program getProgramById(Integer id);
    Program getProgramByName(String name);
    Program createProgram(ProgramRequestDto program);
    Program updateProgram(Integer id, ProgramRequestDto program);
    void deleteProgram(Integer id);
}