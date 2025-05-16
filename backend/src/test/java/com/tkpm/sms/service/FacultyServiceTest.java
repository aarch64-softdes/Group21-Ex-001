package com.tkpm.sms.service;

import com.tkpm.sms.application.dto.request.faculty.FacultyRequestDto;
import com.tkpm.sms.application.service.implementation.FacultyServiceImpl;
import com.tkpm.sms.application.service.interfaces.TextContentService;
import com.tkpm.sms.domain.exception.ResourceNotFoundException;
import com.tkpm.sms.domain.model.Faculty;
import com.tkpm.sms.domain.repository.FacultyRepository;
import com.tkpm.sms.domain.service.TranslatorService;
import com.tkpm.sms.domain.service.validators.FacultyDomainValidator;
import com.tkpm.sms.domain.valueobject.TextContent;
import com.tkpm.sms.domain.valueobject.Translation;
import com.tkpm.sms.infrastructure.mapper.FacultyMapperImpl;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class FacultyServiceTest {

    @Mock
    private FacultyRepository facultyRepository;

    @Mock
    private FacultyDomainValidator facultyValidator;

    @Mock
    private FacultyMapperImpl facultyMapper;

    @Mock
    private TextContentService textContentService;

    @Mock
    private TranslatorService translatorService;

    @InjectMocks
    private FacultyServiceImpl facultyService;

    private Faculty faculty;
    private FacultyRequestDto facultyRequestDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup default translator response
        when(translatorService.getEntityTranslatedName(Faculty.class)).thenReturn("Faculty");

        var textContent = TextContent.builder().id(1).createdAt(LocalDateTime.now())
                .translations(Collections.singletonList(Translation.builder().languageCode("en")
                        .text("Faculty Name").isOriginal(true).build()))
                .build();

        faculty = Faculty.builder().id(1).name(textContent).build();

        facultyRequestDto = FacultyRequestDto.builder().name("Faculty Name").build();
    }

    // @Test
    // void testGetAllFaculties() {
    // BaseCollectionRequest request = new BaseCollectionRequest(1, 10, "id",
    // "ASC");
    // Page<Faculty> page = new PageImpl<>(Collections.emptyList());
    // when(facultyRepository.findAll(any(PageRequest.class))).thenReturn(new
    // PageResponse<>(page));

    // PageResponse<Faculty> response = facultyService.getAllFaculties(request);

    // assertNotNull(response);
    // verify(facultyRepository).findAll(any(PageRequest.class));
    // }

    @Test
    void testGetFacultyById_NotFound() {
        // Setup
        Integer id = 1;
        when(facultyRepository.findById(id)).thenReturn(Optional.empty());

        // Execute & Verify
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> facultyService.getFacultyById(id));

        // Verify the correct parameters were used
        verify(facultyRepository).findById(id);
        verify(translatorService).getEntityTranslatedName(Faculty.class);
    }

    @Test
    void testGetFacultyById_Success() {
        Faculty faculty = new Faculty();
        when(facultyRepository.findById(1)).thenReturn(Optional.of(faculty));

        Faculty result = facultyService.getFacultyById(1);

        assertNotNull(result);
        verify(facultyRepository).findById(1);
    }

    @Test
    void testGetFacultyByName_NotFound() {
        // Setup
        String name = "Test Faculty";
        when(facultyRepository.findByName(name)).thenReturn(Optional.empty());

        // Execute & Verify
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> facultyService.getFacultyByName(name));

        // Verify the correct parameters were used
        verify(facultyRepository).findByName(name);
        verify(translatorService).getEntityTranslatedName(Faculty.class);
    }

    @Test
    void testGetFacultyByName_Success() {
        when(facultyRepository.findByName("Test Faculty")).thenReturn(Optional.of(faculty));

        Faculty result = facultyService.getFacultyByName("Test Faculty");

        assertNotNull(result);
        verify(facultyRepository).findByName("Test Faculty");
    }

    @Test
    void testCreateFaculty() {
        // Given
        doNothing().when(facultyValidator).validateNameUniqueness(anyString());
        when(facultyMapper.toDomain(any(FacultyRequestDto.class))).thenReturn(faculty);
        when(textContentService.createTextContent(anyString())).thenReturn(faculty.getName());
        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);

        // When
        Faculty result = facultyService.createFaculty(facultyRequestDto);

        // Then
        assertNotNull(result);
        assertNotNull(result.getName());
        assertEquals("Faculty Name", result.getDefaultName()); // Use getDefaultName() not getName()
    }

    @Test
    void testCreateFaculty_Success() {
        // Given
        doNothing().when(facultyValidator).validateNameUniqueness(anyString());
        when(facultyMapper.toDomain(any(FacultyRequestDto.class))).thenReturn(faculty);
        when(textContentService.createTextContent(anyString())).thenReturn(faculty.getName());
        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);

        // When
        Faculty result = facultyService.createFaculty(facultyRequestDto);

        // Then
        assertNotNull(result);
        assertNotNull(result.getName());
        assertEquals("Faculty Name", result.getDefaultName());

        verify(facultyValidator).validateNameUniqueness("Faculty Name");
        verify(facultyRepository).save(any(Faculty.class));
    }

    @Test
    void testUpdateFaculty_NotFound() {
        // Setup
        Integer id = 1;
        String updatedName = "Updated Faculty";

        FacultyRequestDto requestDto = new FacultyRequestDto();
        requestDto.setName(updatedName);

        when(facultyRepository.findById(id)).thenReturn(Optional.empty());

        // Execute & Verify
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> facultyService.updateFaculty(id, requestDto));

        verify(facultyValidator).validateNameUniquenessForUpdate(updatedName, id);
        verify(facultyRepository).findById(id);
        verify(translatorService).getEntityTranslatedName(Faculty.class);
        verifyNoInteractions(textContentService, facultyMapper);
        verifyNoMoreInteractions(facultyRepository);
    }

    @Test
    void testUpdateFaculty_Success() {
        doNothing().when(facultyValidator).validateNameUniqueness(anyString());

        when(facultyRepository.findById(1)).thenReturn(Optional.of(faculty));
        when(facultyMapper.toDomain(any(FacultyRequestDto.class))).thenReturn(faculty);
        when(textContentService.updateTextContent(any(), anyString()))
                .thenReturn(faculty.getName());
        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);

        Faculty result = facultyService.updateFaculty(1, facultyRequestDto);

        assertNotNull(result);
        assertEquals("Faculty Name", result.getDefaultName());

        verify(facultyValidator).validateNameUniquenessForUpdate("Faculty Name", 1);
        verify(facultyRepository).save(any(Faculty.class));
    }

    @Test
    @Transactional
    void testDeleteFaculty_Success() {
        when(facultyRepository.findById(1)).thenReturn(Optional.of(faculty));

        facultyService.deleteFaculty(1);

        verify(facultyRepository).delete(faculty);
    }

    @Test
    void testDeleteFaculty_NotFound() {
        // Setup
        Integer id = 1;
        when(facultyRepository.findById(id)).thenReturn(Optional.empty());

        // Execute & Verify
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> facultyService.deleteFaculty(id));

        verify(facultyRepository).findById(id);
        verify(translatorService).getEntityTranslatedName(Faculty.class);
        verifyNoMoreInteractions(facultyRepository);
    }
}