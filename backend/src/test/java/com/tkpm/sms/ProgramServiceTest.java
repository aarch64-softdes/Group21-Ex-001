package com.tkpm.sms;

import com.tkpm.sms.application.dto.request.common.BaseCollectionRequest;
import com.tkpm.sms.application.dto.request.program.ProgramRequestDto;
import com.tkpm.sms.application.mapper.ProgramMapper;
import com.tkpm.sms.application.service.implementation.ProgramServiceImpl;
import com.tkpm.sms.domain.common.PageRequest;
import com.tkpm.sms.domain.common.PageResponse;
import com.tkpm.sms.domain.exception.ResourceNotFoundException;
import com.tkpm.sms.domain.model.Program;
import com.tkpm.sms.domain.repository.ProgramRepository;
import com.tkpm.sms.domain.service.validators.ProgramDomainValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProgramServiceTest {

    @Mock
    private ProgramRepository programRepository;

    @Mock
    private ProgramDomainValidator programDomainValidator;

    @Mock
    private ProgramMapper programMapper;

    @InjectMocks
    private ProgramServiceImpl programService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // @Test
    // void testGetAllPrograms() {
    // BaseCollectionRequest request = new BaseCollectionRequest(1, 10, "id",
    // "ASC");
    // Page<Program> page = new PageImpl<>(Collections.emptyList());
    // when(programRepository.findAll(any(PageRequest.class))).thenReturn(new
    // PageResponse<>(page));

    // PageResponse<Program> response = programService.getAllPrograms(request);

    // assertNotNull(response);
    // verify(programRepository, times(1)).findAll(any(PageRequest.class));
    // }

    @Test
    void testGetProgramById_NotFound() {
        when(programRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> programService.getProgramById(1));
    }

    @Test
    void testGetProgramByName_NotFound() {
        when(programRepository.findByName("Test Program")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> programService.getProgramByName("Test Program"));
    }

    @Test
    void testCreateProgram() {
        ProgramRequestDto requestDto = new ProgramRequestDto();
        requestDto.setName("New Program");

        Program program = new Program();
        when(programMapper.toEntity(requestDto)).thenReturn(program);
        when(programRepository.save(program)).thenReturn(program);

        Program createdProgram = programService.createProgram(requestDto);

        assertNotNull(createdProgram);
        verify(programDomainValidator, times(1)).validateNameUniqueness("New Program");
        verify(programRepository, times(1)).save(program);
    }

    @Test
    void testUpdateProgram_NotFound() {
        when(programRepository.findById(1)).thenReturn(Optional.empty());

        ProgramRequestDto requestDto = new ProgramRequestDto();
        requestDto.setName("Updated Program");

        assertThrows(ResourceNotFoundException.class, () -> programService.updateProgram(1, requestDto));
    }

    @Test
    void testUpdateProgram_Success() {
        ProgramRequestDto requestDto = new ProgramRequestDto();
        requestDto.setName("Updated Program");

        Program program = new Program();
        when(programRepository.findById(1)).thenReturn(Optional.of(program));
        doNothing().when(programDomainValidator).validateNameUniquenessForUpdate("Updated Program", 1);
        doNothing().when(programMapper).updateProgramFromDto(requestDto, program);
        when(programRepository.save(program)).thenReturn(program);

        Program updatedProgram = programService.updateProgram(1, requestDto);

        assertNotNull(updatedProgram);
        verify(programDomainValidator, times(1)).validateNameUniquenessForUpdate("Updated Program", 1);
        verify(programMapper, times(1)).updateProgramFromDto(requestDto, program);
        verify(programRepository, times(1)).save(program);
    }

    @Test
    void testDeleteProgram() {
        Program program = new Program();
        when(programRepository.findById(1)).thenReturn(Optional.of(program));
        when(programRepository.save(program)).thenReturn(program);

        programService.deleteProgram(1);

        assertNotNull(program.getDeletedAt());
        verify(programRepository, times(1)).save(program);
    }
}