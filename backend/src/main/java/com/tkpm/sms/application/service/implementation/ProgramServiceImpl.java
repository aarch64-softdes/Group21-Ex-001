package com.tkpm.sms.application.service.implementation;

import com.tkpm.sms.application.dto.request.common.BaseCollectionRequest;
import com.tkpm.sms.application.dto.request.program.ProgramRequestDto;
import com.tkpm.sms.application.mapper.ProgramMapper;
import com.tkpm.sms.application.service.interfaces.ProgramService;
import com.tkpm.sms.application.service.interfaces.TextContentService;
import com.tkpm.sms.domain.common.PageRequest;
import com.tkpm.sms.domain.common.PageResponse;
import com.tkpm.sms.domain.exception.ResourceNotFoundException;
import com.tkpm.sms.domain.model.Program;
import com.tkpm.sms.domain.repository.ProgramRepository;
import com.tkpm.sms.domain.service.TranslatorService;
import com.tkpm.sms.domain.service.validators.ProgramDomainValidator;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ProgramServiceImpl implements ProgramService {
    ProgramRepository programRepository;
    ProgramDomainValidator programDomainValidator;
    ProgramMapper programMapper;
    TextContentService textContentService;
    TranslatorService translatorService;

    @Override
    public PageResponse<Program> getAllPrograms(BaseCollectionRequest search) {
        PageRequest pageRequest = PageRequest.from(search);

        return programRepository.findAll(pageRequest);
    }

    @Override
    public Program getProgramById(Integer id) {
        return programRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("error.not_found.id",
                        translatorService.getEntityTranslatedName(Program.class), id));
    }

    @Override
    public Program getProgramByName(String name) {
        return programRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("error.not_found.name",
                        translatorService.getEntityTranslatedName(Program.class), name));
    }

    @Override
    @Transactional
    public Program createProgram(ProgramRequestDto programRequestDto) {
        // Use domain service for business validation
        programDomainValidator.validateNameUniqueness(programRequestDto.getName());

        // Convert DTO to domain entity using mapper
        Program program = programMapper.toDomain(programRequestDto);
        program.setName(textContentService.createTextContent(programRequestDto.getName()));

        return programRepository.save(program);
    }

    @Override
    @Transactional
    public Program updateProgram(Integer id, ProgramRequestDto programRequestDto) {
        // Use domain service for business validation
        programDomainValidator.validateNameUniquenessForUpdate(programRequestDto.getName(), id);

        Program program = programRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("error.not_found.id",
                        translatorService.getEntityTranslatedName(Program.class), id));

        programMapper.toDomain(programRequestDto, program);
        program.setName(textContentService.updateTextContent(program.getName(),
                programRequestDto.getName()));

        // Update domain entity using mapper
        return programRepository.save(program);
    }

    @Override
    @Transactional
    public void deleteProgram(Integer id) {
        Program program = programRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("error.not_found.id",
                        translatorService.getEntityTranslatedName(Program.class), id));

        program.setDeletedAt(LocalDate.now());
        programRepository.save(program);
    }
}