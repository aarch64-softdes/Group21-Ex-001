package com.tkpm.sms.service;

import com.tkpm.sms.application.dto.request.subject.SubjectCreateRequestDto;
import com.tkpm.sms.application.dto.request.subject.SubjectUpdateRequestDto;
import com.tkpm.sms.application.mapper.SubjectMapper;
import com.tkpm.sms.application.service.implementation.SubjectServiceImpl;
import com.tkpm.sms.application.service.interfaces.FacultyService;
import com.tkpm.sms.domain.exception.ResourceNotFoundException;
import com.tkpm.sms.domain.exception.SubjectDeletionConstraintException;
import com.tkpm.sms.domain.model.Faculty;
import com.tkpm.sms.domain.model.Subject;
import com.tkpm.sms.domain.repository.SubjectRepository;
import com.tkpm.sms.domain.service.validators.SubjectDomainValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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

    @InjectMocks
    private SubjectServiceImpl subjectService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // @Test
    // @DisplayName("Should find all subjects successfully")
    // void testFindAll() {
    // BaseCollectionRequest request = new BaseCollectionRequest(1, 10, "id",
    // "ASC");
    // Page<Subject> page = new PageImpl<>(Collections.emptyList());
    // when(subjectRepository.findAll(any(PageRequest.class))).thenReturn(new
    // PageResponse<>(page));

    // PageResponse<Subject> response = subjectService.findAll(request);

    // assertNotNull(response);
    // verify(subjectRepository).findAll(any(PageRequest.class));
    // }

    @Test
    @DisplayName("Should get subject by ID successfully")
    void testGetSubjectById_Success() {
        Integer id = 1;
        Subject subject = new Subject();
        when(subjectRepository.findById(id)).thenReturn(Optional.of(subject));

        Subject result = subjectService.getSubjectById(id);

        assertNotNull(result);
        verify(subjectRepository).findById(id);
    }

    @Test
    @DisplayName("Should throw exception when subject not found by ID")
    void testGetSubjectById_NotFound() {
        Integer id = 1;
        when(subjectRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> subjectService.getSubjectById(id));
    }

    @Test
    @DisplayName("Should create subject successfully")
    void testCreateSubject_Success() {
        SubjectCreateRequestDto requestDto = new SubjectCreateRequestDto();
        requestDto.setCode("SUB101");
        requestDto.setName("Test Subject");
        requestDto.setFacultyId(1);
        requestDto.setPrerequisitesId(Collections.emptyList());

        Faculty faculty = new Faculty();
        Subject subject = new Subject();

        when(facultyService.getFacultyById(1)).thenReturn(faculty);
        when(subjectMapper.toSubject(requestDto)).thenReturn(subject);
        when(subjectRepository.findAllByIds(anyList())).thenReturn(Collections.emptyList());
        when(subjectRepository.save(subject)).thenReturn(subject);

        Subject result = subjectService.createSubject(requestDto);

        assertNotNull(result);
        verify(subjectValidator).validateSubjectCodeUniqueness(requestDto.getCode());
        verify(subjectValidator).validateSubjectNameUniqueness(requestDto.getName());
        verify(subjectValidator).validatePrerequisites(requestDto.getPrerequisitesId());
        verify(subjectRepository).save(subject);
    }

    @Test
    @DisplayName("Should update subject successfully")
    void testUpdateSubject_Success() {
        Integer id = 1;
        SubjectUpdateRequestDto requestDto = new SubjectUpdateRequestDto();
        requestDto.setName("Updated Subject");
        requestDto.setFacultyId(1);
        requestDto.setPrerequisitesId(Collections.emptyList());

        Subject subject = new Subject();
        Faculty faculty = new Faculty();

        when(subjectRepository.findById(id)).thenReturn(Optional.of(subject));
        when(facultyService.getFacultyById(1)).thenReturn(faculty);
        when(subjectRepository.findAllByIds(anyList())).thenReturn(Collections.emptyList());
        when(subjectRepository.save(subject)).thenReturn(subject);

        Subject result = subjectService.updateSubject(id, requestDto);

        assertNotNull(result);
        verify(subjectValidator).validateSubjectNameUniquenessForUpdate(requestDto.getName(), id);
        verify(subjectValidator).validatePrerequisites(requestDto.getPrerequisitesId());
        verify(subjectMapper).updateSubjectFromDto(subject, requestDto);
        verify(subjectRepository).save(subject);
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent subject")
    void testUpdateSubject_NotFound() {
        Integer id = 1;
        SubjectUpdateRequestDto requestDto = new SubjectUpdateRequestDto();
        when(subjectRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> subjectService.updateSubject(id, requestDto));
    }

    @Test
    @DisplayName("Should throw exception when trying to delete subject after 30 minutes")
    void testDeleteSubject_ThrowsException_AfterTimeLimit() {
        Integer id = 1;
        Subject subject = new Subject();
        subject.setCreatedAt(LocalDateTime.now().minus(31, ChronoUnit.MINUTES));

        when(subjectRepository.findById(id)).thenReturn(Optional.of(subject));

        assertThrows(SubjectDeletionConstraintException.class,
                () -> subjectService.deleteSubject(id));

        verify(subjectRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent subject")
    void testDeleteSubject_NotFound() {
        Integer id = 1;
        when(subjectRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> subjectService.deleteSubject(id));
    }

    @Test
    @DisplayName("Should deactivate subject successfully")
    void testDeactivateSubject_Success() {
        Integer id = 1;
        Subject subject = new Subject();
        subject.setActive(true);

        when(subjectRepository.findById(id)).thenReturn(Optional.of(subject));
        when(subjectRepository.save(subject)).thenReturn(subject);

        subjectService.deactivateSubject(id);

        assertFalse(subject.isActive());
        verify(subjectRepository).save(subject);
    }

    @Test
    @DisplayName("Should activate subject successfully")
    void testActivateSubject_Success() {
        Integer id = 1;
        Subject subject = new Subject();
        subject.setActive(false);

        when(subjectRepository.findById(id)).thenReturn(Optional.of(subject));
        when(subjectRepository.save(subject)).thenReturn(subject);

        subjectService.activateSubject(id);

        assertTrue(subject.isActive());
        verify(subjectRepository).save(subject);
    }

    @Test
    @DisplayName("Should throw exception when deactivating non-existent subject")
    void testDeactivateSubject_NotFound() {
        Integer id = 1;
        when(subjectRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> subjectService.deactivateSubject(id));
    }

    @Test
    @DisplayName("Should throw exception when activating non-existent subject")
    void testActivateSubject_NotFound() {
        Integer id = 1;
        when(subjectRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> subjectService.activateSubject(id));
    }
}