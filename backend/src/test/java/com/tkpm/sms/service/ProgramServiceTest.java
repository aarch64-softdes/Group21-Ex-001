package com.tkpm.sms.service;

import com.tkpm.sms.application.dto.request.program.ProgramRequestDto;
import com.tkpm.sms.application.mapper.ProgramMapper;
import com.tkpm.sms.application.service.implementation.ProgramServiceImpl;
import com.tkpm.sms.application.service.interfaces.TextContentService;
import com.tkpm.sms.domain.exception.ResourceNotFoundException;
import com.tkpm.sms.domain.model.Program;
import com.tkpm.sms.domain.repository.ProgramRepository;
import com.tkpm.sms.domain.service.DomainEntityNameTranslator;
import com.tkpm.sms.domain.service.validators.ProgramDomainValidator;
import com.tkpm.sms.domain.valueobject.TextContent;
import com.tkpm.sms.domain.valueobject.Translation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProgramServiceTest {

    @Mock
    private ProgramRepository programRepository;

    @Mock
    private ProgramDomainValidator programDomainValidator;

    @Mock
    private ProgramMapper programMapper;
    
    @Mock
    private TextContentService textContentService;
    
    @Mock
    private DomainEntityNameTranslator domainEntityNameTranslator;

    @InjectMocks
    private ProgramServiceImpl programService;
    
    private TextContent textContent;
    private Program program;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // Setup common mock behaviors
        when(domainEntityNameTranslator.getEntityTranslatedName(Program.class)).thenReturn("Program");
        
        // Create test data
        textContent = TextContent.builder()
                .id(1)
                .createdAt(LocalDateTime.now())
                .translations(Collections.singletonList(
                        Translation.builder()
                                .languageCode("en")
                                .text("Program Name")
                                .isOriginal(true)
                                .build()
                ))
                .build();
        
        program = Program.builder()
                .id(1)
                .name(textContent)
                .build();
    }

    // @Test
    // void testGetAllPrograms() {
    //     // Setup
    //     BaseCollectionRequest request = new BaseCollectionRequest();
    //     request.setPage(1);
    //     request.setSize(10);
    //     request.setSortBy("id");
    //     request.setSortDirection("ASC");
        
    //     PageResponse<Program> expectedResponse = new PageResponse<>(
    //         Collections.emptyList(), 0, 0, 0
    //     );
        
    //     when(programRepository.findAll(any(PageRequest.class))).thenReturn(expectedResponse);

    //     // Execute
    //     PageResponse<Program> response = programService.getAllPrograms(request);

    //     // Verify
    //     assertNotNull(response);
    //     verify(programRepository).findAll(any(PageRequest.class));
    // }

    @Test
    void testGetProgramById_NotFound() {
        // Setup
        Integer id = 1;
        when(programRepository.findById(id)).thenReturn(Optional.empty());

        // Execute & Verify
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class, 
            () -> programService.getProgramById(id)
        );
        
        verify(programRepository).findById(id);
        verify(domainEntityNameTranslator).getEntityTranslatedName(Program.class);
    }
    
    @Test
    void testGetProgramById_Success() {
        // Setup
        Integer id = 1;
        when(programRepository.findById(id)).thenReturn(Optional.of(program));
        
        // Execute
        Program result = programService.getProgramById(id);
        
        // Verify
        assertNotNull(result);
        assertEquals(program, result);
        verify(programRepository).findById(id);
    }

    @Test
    void testGetProgramByName_NotFound() {
        // Setup
        String name = "Test Program";
        when(programRepository.findByName(name)).thenReturn(Optional.empty());

        // Execute & Verify
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class, 
            () -> programService.getProgramByName(name)
        );
        
        verify(programRepository).findByName(name);
        verify(domainEntityNameTranslator).getEntityTranslatedName(Program.class);
    }
    
    @Test
    void testGetProgramByName_Success() {
        // Setup
        String name = "Program Name";
        when(programRepository.findByName(name)).thenReturn(Optional.of(program));
        
        // Execute
        Program result = programService.getProgramByName(name);
        
        // Verify
        assertNotNull(result);
        assertEquals(program, result);
        verify(programRepository).findByName(name);
    }

    @Test
    void testCreateProgram() {
        // Setup
        String programName = "New Program";
        ProgramRequestDto requestDto = new ProgramRequestDto();
        requestDto.setName(programName);
        
        Program newProgram = new Program();
        
        when(textContentService.createTextContent(programName)).thenReturn(textContent);
        when(programMapper.toDomain(requestDto)).thenReturn(newProgram);
        when(programRepository.save(any(Program.class))).thenReturn(program);
        doNothing().when(programDomainValidator).validateNameUniqueness(programName);

        // Execute
        Program createdProgram = programService.createProgram(requestDto);

        // Verify
        assertNotNull(createdProgram);
        assertEquals(program, createdProgram);
        
        verify(programDomainValidator).validateNameUniqueness(programName);
        verify(programMapper).toDomain(requestDto);
        verify(textContentService).createTextContent(programName);
        verify(programRepository).save(any(Program.class));
    }

    @Test
    void testUpdateProgram_NotFound() {
        // Setup
        Integer id = 1;
        String updatedName = "Updated Program";
        
        ProgramRequestDto requestDto = new ProgramRequestDto();
        requestDto.setName(updatedName);
        
        when(programRepository.findById(id)).thenReturn(Optional.empty());
        doNothing().when(programDomainValidator).validateNameUniquenessForUpdate(updatedName, id);

        // Execute & Verify
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class, 
            () -> programService.updateProgram(id, requestDto)
        );
        
        verify(programDomainValidator).validateNameUniquenessForUpdate(updatedName, id);
        verify(programRepository).findById(id);
        verify(domainEntityNameTranslator).getEntityTranslatedName(Program.class);
        verifyNoInteractions(textContentService, programMapper);
        verifyNoMoreInteractions(programRepository);
    }

    @Test
    void testUpdateProgram_Success() {
        // Setup
        Integer id = 1;
        String updatedName = "Updated Program";
        
        ProgramRequestDto requestDto = new ProgramRequestDto();
        requestDto.setName(updatedName);
        
        Program existingProgram = new Program();
        existingProgram.setId(id);
        existingProgram.setName(textContent);
        
        when(programRepository.findById(id)).thenReturn(Optional.of(existingProgram));
        doNothing().when(programDomainValidator).validateNameUniquenessForUpdate(updatedName, id);
        doNothing().when(programMapper).toDomain(requestDto, existingProgram);
        when(textContentService.updateTextContent(textContent, updatedName)).thenReturn(textContent);
        when(programRepository.save(existingProgram)).thenReturn(existingProgram);

        // Execute
        Program result = programService.updateProgram(id, requestDto);

        // Verify
        assertNotNull(result);
        assertEquals(existingProgram, result);
        
        verify(programDomainValidator).validateNameUniquenessForUpdate(updatedName, id);
        verify(programRepository).findById(id);
        verify(programMapper).toDomain(requestDto, existingProgram);
        verify(textContentService).updateTextContent(textContent, updatedName);
        verify(programRepository).save(existingProgram);
    }

    @Test
    void testDeleteProgram_NotFound() {
        // Setup
        Integer id = 1;
        when(programRepository.findById(id)).thenReturn(Optional.empty());

        // Execute & Verify
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class, 
            () -> programService.deleteProgram(id)
        );
        
        verify(programRepository).findById(id);
        verify(domainEntityNameTranslator).getEntityTranslatedName(Program.class);
        verifyNoMoreInteractions(programRepository);
    }

    @Test
    void testDeleteProgram_Success() {
        // Setup
        Integer id = 1;
        Program program = new Program();
        program.setId(id);
        
        when(programRepository.findById(id)).thenReturn(Optional.of(program));
        when(programRepository.save(program)).thenReturn(program);

        // Execute
        programService.deleteProgram(id);

        // Verify
        assertNotNull(program.getDeletedAt());
        assertEquals(LocalDate.now(), program.getDeletedAt());
        verify(programRepository).findById(id);
        verify(programRepository).save(program);
    }
}