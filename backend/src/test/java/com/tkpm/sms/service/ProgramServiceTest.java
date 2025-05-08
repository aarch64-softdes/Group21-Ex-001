package com.tkpm.sms.service;

import com.tkpm.sms.application.dto.request.program.ProgramRequestDto;
import com.tkpm.sms.application.mapper.ProgramMapper;
import com.tkpm.sms.application.service.implementation.ProgramServiceImpl;
import com.tkpm.sms.application.service.implementation.TextContentServiceImpl;
import com.tkpm.sms.domain.exception.ResourceNotFoundException;
import com.tkpm.sms.domain.model.Program;
import com.tkpm.sms.domain.repository.ProgramRepository;
import com.tkpm.sms.domain.repository.TextContentRepository;
import com.tkpm.sms.domain.service.validators.ProgramDomainValidator;
import com.tkpm.sms.domain.valueobject.TextContent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProgramServiceTest {

    @Mock
    private ProgramRepository programRepository;

    @Mock
    private TextContentRepository textContentRepository;

    @Mock
    private ProgramDomainValidator programDomainValidator;

    @Mock
    private ProgramMapper programMapper;

    @InjectMocks
    private ProgramServiceImpl programService;

    @Mock
    private TextContentServiceImpl textContentService;

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
        saveTextContent();
        ProgramRequestDto requestDto = new ProgramRequestDto();
        requestDto.setName("New Program");

        Program program = Program.builder().id(1)
                .name(textContentService.createTextContent("New Program")).build();
        when(programMapper.toDomain(requestDto)).thenReturn(program);
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
        saveTextContent();
        ProgramRequestDto requestDto = new ProgramRequestDto();
        requestDto.setName("Updated Program");

        Program program = Program.builder().id(1)
                .name(textContentService.createTextContent("New Program")).build();
        when(programRepository.findById(1)).thenReturn(Optional.of(program));
        doNothing().when(programDomainValidator).validateNameUniquenessForUpdate("Updated Program",
                1);
        doNothing().when(programMapper).toDomain(requestDto, program);
        when(programRepository.save(program)).thenReturn(program);

        Program updatedProgram = programService.updateProgram(1, requestDto);

        assertNotNull(updatedProgram);
        verify(programDomainValidator, times(1)).validateNameUniquenessForUpdate("Updated Program",
                1);
        verify(programMapper, times(1)).toDomain(requestDto, program);
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

    private void saveTextContent() {
        when(textContentRepository.save(any())).thenAnswer(invocation -> {
            Object[] args = invocation.getArguments();
            return (TextContent) args[0];
        });
    }
}