package com.tkpm.sms.application.service.implementation;

import com.tkpm.sms.application.dto.request.common.BaseCollectionRequest;
import com.tkpm.sms.application.dto.request.program.ProgramRequestDto;
import com.tkpm.sms.application.mapper.ProgramMapper;
import com.tkpm.sms.application.service.interfaces.ProgramService;
import com.tkpm.sms.domain.common.PageRequest;
import com.tkpm.sms.domain.common.PageResponse;
import com.tkpm.sms.domain.exception.ResourceNotFoundException;
import com.tkpm.sms.domain.model.Program;
import com.tkpm.sms.domain.repository.ProgramRepository;
import com.tkpm.sms.domain.service.validators.ProgramDomainValidator;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ProgramServiceImpl implements ProgramService {
    ProgramRepository programRepository;
    ProgramDomainValidator programDomainValidator;
    ProgramMapper programMapper;

    @Override
    public PageResponse<Program> getAllPrograms(BaseCollectionRequest search) {
        PageRequest pageRequest = PageRequest.from(search);

        return programRepository.findAll(pageRequest);
    }

    @Override
    public Program getProgramById(Integer id) {
        return programRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(
                String.format("Program with id %s not found", id)));
    }

    @Override
    public Program getProgramByName(String name) {
        return programRepository.findByName(name).orElseThrow(() -> new ResourceNotFoundException(
                String.format("Program with name %s not found", name)));
    }

    @Override
    @Transactional
    public Program createProgram(ProgramRequestDto programRequestDto) {
        // Use domain service for business validation
        programDomainValidator.validateNameUniqueness(programRequestDto.getName());

        // Convert DTO to domain entity using mapper
        Program program = programMapper.toEntity(programRequestDto);
        return programRepository.save(program);
    }

    @Override
    @Transactional
    public Program updateProgram(Integer id, ProgramRequestDto programRequestDto) {
        // Use domain service for business validation
        programDomainValidator.validateNameUniquenessForUpdate(programRequestDto.getName(), id);

        Program programToUpdate = programRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Program with id %s not found", id)));

        // Update domain entity using mapper
        programMapper.updateProgramFromDto(programRequestDto, programToUpdate);
        return programRepository.save(programToUpdate);
    }

    @Override
    @Transactional
    public void deleteProgram(Integer id) {
        Program program = programRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Program with id %s not found", id)));

        programRepository.delete(program);
    }
}