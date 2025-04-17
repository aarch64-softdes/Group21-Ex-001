package com.tkpm.sms;

import com.tkpm.sms.application.dto.request.common.BaseCollectionRequest;
import com.tkpm.sms.application.dto.request.faculty.FacultyRequestDto;
import com.tkpm.sms.application.service.implementation.FacultyServiceImpl;
import com.tkpm.sms.domain.common.PageRequest;
import com.tkpm.sms.domain.common.PageResponse;
import com.tkpm.sms.domain.exception.ResourceNotFoundException;
import com.tkpm.sms.domain.model.Faculty;
import com.tkpm.sms.domain.repository.FacultyRepository;
import com.tkpm.sms.domain.service.validators.FacultyDomainValidator;
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

class FacultyServiceImplTest {

    @Mock
    private FacultyRepository facultyRepository;

    @Mock
    private FacultyDomainValidator facultyValidator;

    @InjectMocks
    private FacultyServiceImpl facultyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // @Test
    // void testGetAllFaculties() {
    // BaseCollectionRequest request = new BaseCollectionRequest(1, 10, "id",
    // "ASC");
    // Page<Faculty> page = new PageImpl<>(Collections.emptyList());
    // when(facultyRepository.findAll(any(PageRequest.class))).thenReturn(page);

    // PageResponse<Faculty> response = facultyService.getAllFaculties(request);

    // assertNotNull(response);
    // verify(facultyRepository, times(1)).findAll(any(PageRequest.class));
    // }

    @Test
    void testGetFacultyById_NotFound() {
        when(facultyRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> facultyService.getFacultyById(1));
    }

    @Test
    void testGetFacultyByName_NotFound() {
        when(facultyRepository.findByName("Test Faculty")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> facultyService.getFacultyByName("Test Faculty"));
    }

    @Test
    void testCreateFaculty() {
        FacultyRequestDto requestDto = new FacultyRequestDto();
        requestDto.setName("New Faculty");

        doNothing().when(facultyValidator).validateNameUniqueness("New Faculty");
        Faculty faculty = Faculty.builder().name("New Faculty").build();
        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);

        Faculty createdFaculty = facultyService.createFaculty(requestDto);

        assertNotNull(createdFaculty);
        assertEquals("New Faculty", createdFaculty.getName());
        verify(facultyRepository, times(1)).save(any(Faculty.class));
    }

    @Test
    void testUpdateFaculty_NotFound() {
        when(facultyRepository.findById(1)).thenReturn(Optional.empty());

        FacultyRequestDto requestDto = new FacultyRequestDto();
        requestDto.setName("Updated Faculty");

        assertThrows(ResourceNotFoundException.class, () -> facultyService.updateFaculty(1, requestDto));
    }

    @Test
    void testDeleteFaculty() {
        Faculty faculty = Faculty.builder().id(1).name("Test Faculty").build();
        when(facultyRepository.findById(1)).thenReturn(Optional.of(faculty));
        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);

        facultyService.deleteFaculty(1);

        assertNotNull(faculty.getDeletedAt());
        verify(facultyRepository, times(1)).save(any(Faculty.class));
    }
}