package com.tkpm.sms;

import com.tkpm.sms.application.dto.request.common.BaseCollectionRequest;
import com.tkpm.sms.application.dto.request.course.CourseCreateRequestDto;
import com.tkpm.sms.application.dto.request.course.CourseUpdateRequestDto;
import com.tkpm.sms.application.mapper.CourseMapper;
import com.tkpm.sms.application.service.implementation.CourseServiceImpl;
import com.tkpm.sms.domain.common.PageResponse;
import com.tkpm.sms.domain.exception.ResourceNotFoundException;
import com.tkpm.sms.domain.exception.SubjectDeactivatedException;
import com.tkpm.sms.domain.model.Course;
import com.tkpm.sms.domain.model.Program;
import com.tkpm.sms.domain.model.Subject;
import com.tkpm.sms.domain.repository.CourseRepository;
import com.tkpm.sms.domain.repository.ProgramRepository;
import com.tkpm.sms.domain.repository.SubjectRepository;
import com.tkpm.sms.domain.service.validators.CourseDomainValidator;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CourseServiceImplTest {

    @Mock
    private CourseMapper courseMapper;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private ProgramRepository programRepository;

    @Mock
    private SubjectRepository subjectRepository;

    @Mock
    private CourseDomainValidator courseValidator;

    @InjectMocks
    private CourseServiceImpl courseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // @Test
    // void testFindAll() {
    // // BaseCollectionRequest request = new BaseCollectionRequest(1, 10, "id",
    // // "ASC");
    // Page<Course> page = new PageImpl<>(Collections.emptyList());
    // when(courseRepository.findAll(any(PageRequest.class))).thenReturn(page);

    // PageResponse<Course> response = courseService.findAll(request);

    // assertNotNull(response);
    // verify(courseRepository, times(1)).findAll(any(PageRequest.class));
    // }

    @Test
    void testGetCourseById_NotFound() {
        when(courseRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> courseService.getCourseById(1));
    }

    @Test
    void testCreateCourse_SubjectNotActive() {
        CourseCreateRequestDto createRequestDto = new CourseCreateRequestDto();
        createRequestDto.setSubjectId(1);
        createRequestDto.setProgramId(1);

        Subject subject = mock(Subject.class);
        when(subject.isActive()).thenReturn(false);
        when(subjectRepository.findById(1)).thenReturn(Optional.of(subject));
        when(programRepository.findById(1)).thenReturn(Optional.of(mock(Program.class)));

        assertThrows(SubjectDeactivatedException.class, () -> courseService.createCourse(createRequestDto));
    }

    @Test
    void testUpdateCourse_NotFound() {
        when(courseRepository.findById(1)).thenReturn(Optional.empty());

        CourseUpdateRequestDto updateRequestDto = new CourseUpdateRequestDto();
        assertThrows(EntityNotFoundException.class, () -> courseService.updateCourse(1, updateRequestDto));
    }

    @Test
    void testDeleteCourse() {
        doNothing().when(courseRepository).deleteById(1);

        courseService.deleteCourse(1);

        verify(courseRepository, times(1)).deleteById(1);
    }
}