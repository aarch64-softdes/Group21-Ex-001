package com.tkpm.sms.service;

import com.tkpm.sms.application.dto.request.subject.SubjectCreateRequestDto;
import com.tkpm.sms.application.dto.request.subject.SubjectUpdateRequestDto;
import com.tkpm.sms.application.mapper.SubjectMapper;
import com.tkpm.sms.application.service.implementation.SubjectServiceImpl;
import com.tkpm.sms.application.service.interfaces.FacultyService;
import com.tkpm.sms.application.service.interfaces.TextContentService;
import com.tkpm.sms.domain.exception.ResourceNotFoundException;
import com.tkpm.sms.domain.exception.SubjectDeletionConstraintException;
import com.tkpm.sms.domain.model.Faculty;
import com.tkpm.sms.domain.model.Subject;
import com.tkpm.sms.domain.repository.SubjectRepository;
import com.tkpm.sms.domain.service.DomainEntityNameTranslator;
import com.tkpm.sms.domain.service.validators.SubjectDomainValidator;
import com.tkpm.sms.domain.valueobject.TextContent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@DisplayName("SubjectService Tests")
class SubjectServiceTest {
    @Mock
    private SubjectRepository subjectRepository;

    @Mock
    private SubjectDomainValidator subjectValidator;

    @Mock
    private SubjectMapper subjectMapper;

    @Mock
    private FacultyService facultyService;
    
    @Mock
    private TextContentService textContentService;
    
    @Mock
    private DomainEntityNameTranslator domainEntityNameTranslator;

    @InjectMocks
    private SubjectServiceImpl subjectService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // Set up the translator
        when(domainEntityNameTranslator.getEntityTranslatedName(Subject.class)).thenReturn("Subject");
        
        // Set up the deletableDuration value (default is 30 minutes)
        ReflectionTestUtils.setField(subjectService, "deletableDuration", 30);
        ReflectionTestUtils.setField(subjectService, "DEFAULT_LANGUAGE", "en");
    }

    // @Test
    // @DisplayName("Should find all subjects successfully")
    // void testFindAll() {
    //     // Setup
    //     BaseCollectionRequest request = new BaseCollectionRequest();
    //     request.setPage(1);
    //     request.setSize(10);
    //     request.setSortBy("id");
    //     request.setSortDirection("ASC");
        
    //     PageResponse<Subject> expectedResponse = new PageResponse<>(
    //         Collections.emptyList(), 0, 0, 0
    //     );
        
    //     when(subjectRepository.findAll(any(PageRequest.class))).thenReturn(expectedResponse);

    //     // Execute
    //     PageResponse<Subject> response = subjectService.findAll(request);

    //     // Verify
    //     assertNotNull(response);
    //     verify(subjectRepository).findAll(any(PageRequest.class));
    // }

    @Test
    @DisplayName("Should get subject by ID successfully")
    void testGetSubjectById_Success() {
        // Setup
        Integer id = 1;
        Subject subject = new Subject();
        when(subjectRepository.findById(id)).thenReturn(Optional.of(subject));

        // Execute
        Subject result = subjectService.getSubjectById(id);

        // Verify
        assertNotNull(result);
        assertEquals(subject, result);
        verify(subjectRepository).findById(id);
    }

    @Test
    @DisplayName("Should throw exception when subject not found by ID")
    void testGetSubjectById_NotFound() {
        // Setup
        Integer id = 1;
        when(subjectRepository.findById(id)).thenReturn(Optional.empty());

        // Execute & Verify
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, 
                () -> subjectService.getSubjectById(id));
        
        verify(subjectRepository).findById(id);
        verify(domainEntityNameTranslator).getEntityTranslatedName(Subject.class);
    }

    @Test
    @DisplayName("Should create subject successfully")
    void testCreateSubject_Success() {
        // Setup
        SubjectCreateRequestDto requestDto = new SubjectCreateRequestDto();
        requestDto.setCode("SUB101");
        requestDto.setName("Test Subject");
        requestDto.setDescription("Test Description");
        requestDto.setFacultyId(1);
        requestDto.setPrerequisitesId(Collections.emptyList());

        Faculty faculty = new Faculty();
        Subject subject = new Subject();
        TextContent nameTextContent = new TextContent();
        TextContent descTextContent = new TextContent();

        when(facultyService.getFacultyById(1)).thenReturn(faculty);
        when(subjectMapper.toSubject(requestDto)).thenReturn(subject);
        when(textContentService.createTextContent(requestDto.getName())).thenReturn(nameTextContent);
        when(textContentService.createTextContent(requestDto.getDescription())).thenReturn(descTextContent);
        when(subjectRepository.findAllByIds(anyList())).thenReturn(Collections.emptyList());
        when(subjectRepository.save(any(Subject.class))).thenReturn(subject);

        // Execute
        Subject result = subjectService.createSubject(requestDto);

        // Verify
        assertNotNull(result);
        verify(subjectValidator).validateSubjectCodeUniqueness(requestDto.getCode());
        verify(subjectValidator).validateSubjectNameUniqueness(requestDto.getName());
        verify(facultyService).getFacultyById(1);
        verify(textContentService).createTextContent(requestDto.getName());
        verify(textContentService).createTextContent(requestDto.getDescription());
        verify(subjectRepository).save(any(Subject.class));
        assertEquals(nameTextContent, subject.getName());
        assertEquals(descTextContent, subject.getDescription());
        assertTrue(subject.isActive());
    }

    @Test
    @DisplayName("Should update subject successfully")
    void testUpdateSubject_Success() {
        // Setup
        Integer id = 1;
        SubjectUpdateRequestDto requestDto = new SubjectUpdateRequestDto();
        requestDto.setName("Updated Subject");
        requestDto.setDescription("Updated Description");
        requestDto.setFacultyId(1);
        requestDto.setPrerequisitesId(Collections.emptyList());

        Subject subject = new Subject();
        TextContent nameTextContent = new TextContent();
        TextContent descTextContent = new TextContent();
        subject.setName(nameTextContent);
        subject.setDescription(descTextContent);
        
        TextContent updatedNameTextContent = new TextContent();
        TextContent updatedDescTextContent = new TextContent();
        
        Faculty faculty = new Faculty();

        when(subjectRepository.findById(id)).thenReturn(Optional.of(subject));
        when(facultyService.getFacultyById(1)).thenReturn(faculty);
        when(textContentService.updateTextContent(nameTextContent, requestDto.getName()))
                .thenReturn(updatedNameTextContent);
        when(textContentService.updateTextContent(descTextContent, requestDto.getDescription()))
                .thenReturn(updatedDescTextContent);
        when(subjectRepository.findAllByIds(anyList())).thenReturn(Collections.emptyList());
        when(subjectRepository.save(any(Subject.class))).thenReturn(subject);

        // Execute
        Subject result = subjectService.updateSubject(id, requestDto);

        // Verify
        assertNotNull(result);
        verify(subjectValidator).validateSubjectNameUniquenessForUpdate(requestDto.getName(), id);
        verify(subjectValidator).validatePrerequisites(requestDto.getPrerequisitesId());
        verify(subjectMapper).updateSubjectFromDto(subject, requestDto);
        verify(textContentService).updateTextContent(nameTextContent, requestDto.getName());
        verify(textContentService).updateTextContent(descTextContent, requestDto.getDescription());
        verify(subjectRepository).save(subject);
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent subject")
    void testUpdateSubject_NotFound() {
        // Setup
        Integer id = 1;
        SubjectUpdateRequestDto requestDto = new SubjectUpdateRequestDto();
        requestDto.setName("Updated Subject");
        
        when(subjectRepository.findById(id)).thenReturn(Optional.empty());

        // Execute & Verify
        assertThrows(ResourceNotFoundException.class,
                () -> subjectService.updateSubject(id, requestDto));
        
        verify(subjectValidator).validateSubjectNameUniquenessForUpdate(requestDto.getName(), id);
        verify(subjectRepository).findById(id);
        verify(domainEntityNameTranslator).getEntityTranslatedName(Subject.class);
        verifyNoMoreInteractions(subjectRepository);
    }

    @Test
    @DisplayName("Should delete subject successfully when within time limit")
    void testDeleteSubject_Success() {
        // Setup
        Integer id = 1;
        Subject subject = new Subject();
        subject.setCode("SUB101");
        subject.setCreatedAt(LocalDateTime.now().minus(15, ChronoUnit.MINUTES));

        when(subjectRepository.findById(id)).thenReturn(Optional.of(subject));
        doNothing().when(subjectValidator).validateSubjectForDeletionAndDeactivation(id);

        // Execute
        subjectService.deleteSubject(id);

        // Verify
        verify(subjectRepository).findById(id);
        verify(subjectValidator).validateSubjectForDeletionAndDeactivation(id);
        verify(subjectRepository).delete(subject);
    }

    @Test
    @DisplayName("Should throw exception when trying to delete subject after time limit")
    void testDeleteSubject_ThrowsException_AfterTimeLimit() {
        // Setup
        Integer id = 1;
        Subject subject = new Subject();
        subject.setCode("SUB101");
        subject.setCreatedAt(LocalDateTime.now().minus(31, ChronoUnit.MINUTES));

        when(subjectRepository.findById(id)).thenReturn(Optional.of(subject));

        // Execute & Verify
        SubjectDeletionConstraintException exception = assertThrows(SubjectDeletionConstraintException.class,
                () -> subjectService.deleteSubject(id));
        
        verify(subjectRepository).findById(id);
        verify(subjectRepository, never()).delete(any(Subject.class));
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent subject")
    void testDeleteSubject_NotFound() {
        // Setup
        Integer id = 1;
        when(subjectRepository.findById(id)).thenReturn(Optional.empty());

        // Execute & Verify
        assertThrows(ResourceNotFoundException.class, () -> subjectService.deleteSubject(id));
        
        verify(subjectRepository).findById(id);
        verify(domainEntityNameTranslator).getEntityTranslatedName(Subject.class);
        verifyNoMoreInteractions(subjectRepository);
    }

    @Test
    @DisplayName("Should deactivate subject successfully")
    void testDeactivateSubject_Success() {
        // Setup
        Integer id = 1;
        Subject subject = new Subject();
        subject.setActive(true);

        when(subjectRepository.findById(id)).thenReturn(Optional.of(subject));
        doNothing().when(subjectValidator).validateSubjectForDeletionAndDeactivation(id);
        when(subjectRepository.save(subject)).thenReturn(subject);

        // Execute
        subjectService.deactivateSubject(id);

        // Verify
        assertFalse(subject.isActive());
        verify(subjectRepository).findById(id);
        verify(subjectValidator).validateSubjectForDeletionAndDeactivation(id);
        verify(subjectRepository).save(subject);
    }

    @Test
    @DisplayName("Should activate subject successfully")
    void testActivateSubject_Success() {
        // Setup
        Integer id = 1;
        Subject subject = new Subject();
        subject.setActive(false);

        when(subjectRepository.findById(id)).thenReturn(Optional.of(subject));
        when(subjectRepository.save(subject)).thenReturn(subject);

        // Execute
        subjectService.activateSubject(id);

        // Verify
        assertTrue(subject.isActive());
        verify(subjectRepository).findById(id);
        verify(subjectRepository).save(subject);
    }

    @Test
    @DisplayName("Should throw exception when deactivating non-existent subject")
    void testDeactivateSubject_NotFound() {
        // Setup
        Integer id = 1;
        when(subjectRepository.findById(id)).thenReturn(Optional.empty());

        // Execute & Verify
        assertThrows(ResourceNotFoundException.class, () -> subjectService.deactivateSubject(id));
        
        verify(subjectRepository).findById(id);
        verify(domainEntityNameTranslator).getEntityTranslatedName(Subject.class);
        verifyNoMoreInteractions(subjectRepository);
    }

    @Test
    @DisplayName("Should throw exception when activating non-existent subject")
    void testActivateSubject_NotFound() {
        // Setup
        Integer id = 1;
        when(subjectRepository.findById(id)).thenReturn(Optional.empty());

        // Execute & Verify
        assertThrows(ResourceNotFoundException.class, () -> subjectService.activateSubject(id));
        
        verify(subjectRepository).findById(id);
        verify(domainEntityNameTranslator).getEntityTranslatedName(Subject.class);
        verifyNoMoreInteractions(subjectRepository);
    }
}