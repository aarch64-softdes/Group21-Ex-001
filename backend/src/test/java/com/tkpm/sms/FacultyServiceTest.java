package com.tkpm.sms;

import com.tkpm.sms.application.dto.request.faculty.FacultyRequestDto;
import com.tkpm.sms.application.service.implementation.FacultyServiceImpl;
import com.tkpm.sms.application.service.interfaces.TextContentService;
import com.tkpm.sms.domain.exception.ResourceNotFoundException;
import com.tkpm.sms.domain.model.Faculty;
import com.tkpm.sms.domain.repository.FacultyRepository;
import com.tkpm.sms.domain.service.validators.FacultyDomainValidator;

import jakarta.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FacultyServiceTest {

    @Mock
    private FacultyRepository facultyRepository;

    @Mock
    private FacultyDomainValidator facultyValidator;

    @InjectMocks
    private FacultyServiceImpl facultyService;

    @InjectMocks
    TextContentService textContentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
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
        when(facultyRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> facultyService.getFacultyById(1));
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
        when(facultyRepository.findByName("Test Faculty")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> facultyService.getFacultyByName("Test Faculty"));
    }

    @Test
    void testGetFacultyByName_Success() {
        Faculty faculty = new Faculty();
        when(facultyRepository.findByName("Test Faculty")).thenReturn(Optional.of(faculty));

        Faculty result = facultyService.getFacultyByName("Test Faculty");

        assertNotNull(result);
        verify(facultyRepository).findByName("Test Faculty");
    }

    @Test
    void testCreateFaculty() {
        FacultyRequestDto requestDto = new FacultyRequestDto();
        requestDto.setName("New Faculty");

        doNothing().when(facultyValidator).validateNameUniqueness("New Faculty");
        Faculty faculty = Faculty.builder()
                .name(textContentService.createTextContent("New Faculty")).build();
        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);

        Faculty createdFaculty = facultyService.createFaculty(requestDto);

        assertNotNull(createdFaculty);
        assertEquals("New Faculty", createdFaculty.getName());
        verify(facultyRepository, times(1)).save(any(Faculty.class));
    }

    @Test
    void testCreateFaculty_Success() {
        FacultyRequestDto requestDto = new FacultyRequestDto();
        requestDto.setName("New Faculty");

        Faculty faculty = Faculty.builder()
                .name(textContentService.createTextContent("New Faculty")).build();
        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);

        Faculty result = facultyService.createFaculty(requestDto);

        assertNotNull(result);
        assertEquals("New Faculty", result.getName());
        verify(facultyValidator).validateNameUniqueness("New Faculty");
        verify(facultyRepository).save(any(Faculty.class));
    }

    @Test
    void testUpdateFaculty_NotFound() {
        when(facultyRepository.findById(1)).thenReturn(Optional.empty());

        FacultyRequestDto requestDto = new FacultyRequestDto();
        requestDto.setName("Updated Faculty");

        assertThrows(ResourceNotFoundException.class, () -> facultyService.updateFaculty(1, requestDto));
    }

    @Test
    void testUpdateFaculty_Success() {
        FacultyRequestDto requestDto = new FacultyRequestDto();
        requestDto.setName("Updated Faculty");

        Faculty faculty = Faculty.builder().id(1)
                .name(textContentService.createTextContent("Old Faculty")).build();
        when(facultyRepository.findById(1)).thenReturn(Optional.of(faculty));
        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);

        Faculty result = facultyService.updateFaculty(1, requestDto);

        assertNotNull(result);
        assertEquals("Updated Faculty", result.getName());
        verify(facultyValidator).validateNameUniquenessForUpdate("Updated Faculty", 1);
        verify(facultyRepository).save(any(Faculty.class));
    }

    @Test
    @Transactional
    void testDeleteFaculty() {
        Faculty faculty = Faculty.builder().id(1)
                .name(textContentService.createTextContent("Faculty to delete")).build();
        when(facultyRepository.findById(1)).thenReturn(Optional.of(faculty));
        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);

        facultyService.deleteFaculty(1);

        verify(facultyRepository).delete(faculty);
    }
}