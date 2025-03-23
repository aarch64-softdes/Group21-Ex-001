package com.tkpm.sms.service;

import com.tkpm.sms.dto.request.program.ProgramRequestDto;
import com.tkpm.sms.dto.request.common.BaseCollectionRequest;
import com.tkpm.sms.entity.Program;
import com.tkpm.sms.exceptions.ApplicationException;
import com.tkpm.sms.exceptions.ErrorCode;
import com.tkpm.sms.repository.ProgramRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ProgramService {
    ProgramRepository programRepository;

    public Page<Program> getAllPrograms(BaseCollectionRequest search) {
        Pageable pageable = PageRequest.of(
                search.getPage() - 1,
                search.getSize(),
                Sort.by(
                        search.getSortDirection().equalsIgnoreCase("desc")
                                ? Sort.Direction.DESC
                                : Sort.Direction.ASC,
                        search.getSortBy()));

        return programRepository.findAll(pageable);
    }

    public Program getProgramById(Integer id) {
        return programRepository.findById(id).orElseThrow(() -> new ApplicationException(
                ErrorCode.NOT_FOUND.withMessage(
                        String.format("Program with id %s not found", id))));
    }

    public Program getProgramByName(String name) {
        return programRepository.findProgramByName(name).orElseThrow(() -> new ApplicationException(
                ErrorCode.NOT_FOUND.withMessage(
                        String.format("Program with name %s not found", name))));
    }

    @Transactional
    public Program createProgram(ProgramRequestDto program) {
        if (programRepository.existsProgramByName(program.getName())) {
            throw new ApplicationException(ErrorCode.CONFLICT.withMessage(
                    String.format("Program with name %s already existed", program.getName())));
        }

        var newProgram = Program.builder().name(program.getName()).build();

        return programRepository.save(newProgram);
    }

    @Transactional
    public Program updateProgram(Integer id, ProgramRequestDto program) {
        if (programRepository.existsProgramByName(program.getName())) {
            throw new ApplicationException(ErrorCode.CONFLICT.withMessage(
                    String.format("Program with name %s already existed", program.getName())));
        }

        Program programToUpdate = programRepository.findById(id).orElseThrow(
                () -> new ApplicationException(ErrorCode.NOT_FOUND.withMessage(
                        String.format("Program with id %s not found", id))));
        programToUpdate.setName(program.getName());

        return programRepository.save(programToUpdate);
    }

    @Transactional
    public void deleteProgram(Integer id) {
        var program = programRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(
                        ErrorCode.NOT_FOUND.withMessage(
                                String.format("Program with id %s not found", id))));

        program.setDeletedAt(LocalDate.now());
        programRepository.save(program);
    }
}
