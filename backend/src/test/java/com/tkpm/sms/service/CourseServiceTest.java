package com.tkpm.sms.service;

import com.tkpm.sms.application.dto.request.course.CourseCreateRequestDto;
import com.tkpm.sms.application.dto.request.course.CourseUpdateRequestDto;
import com.tkpm.sms.application.mapper.CourseMapper;
import com.tkpm.sms.application.service.implementation.CourseServiceImpl;
import com.tkpm.sms.domain.exception.ResourceNotFoundException;
import com.tkpm.sms.domain.exception.SubjectDeactivatedException;
import com.tkpm.sms.domain.model.Course;
import com.tkpm.sms.domain.model.Program;
import com.tkpm.sms.domain.model.Subject;
import com.tkpm.sms.domain.repository.CourseRepository;
import com.tkpm.sms.domain.repository.ProgramRepository;
import com.tkpm.sms.domain.repository.SubjectRepository;
import com.tkpm.sms.domain.service.DomainEntityNameTranslator;
import com.tkpm.sms.domain.service.validators.CourseDomainValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CourseServiceTest {

    @Mock
    private CourseMapper courseMapper;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private ProgramRepository programRepository;

    @Mock
    private SubjectRepository subjectRepository;

    @Mock
    private DomainEntityNameTranslator domainEntityNameTranslator;

    @Mock
    private CourseDomainValidator courseValidator;

    @InjectMocks
    private CourseServiceImpl courseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup common mock behaviors
        when(domainEntityNameTranslator.getEntityTranslatedName(Course.class)).thenReturn("Course");
        when(domainEntityNameTranslator.getEntityTranslatedName(Program.class)).thenReturn("Program");
        when(domainEntityNameTranslator.getEntityTranslatedName(Subject.class)).thenReturn("Subject");
    }

    // @Test
    // void testFindAll() {
    // BaseCollectionRequest request = new BaseCollectionRequest(1, 10, "id",
    // "ASC");
    // Page<Course> page = new PageImpl<>(Collections.emptyList());
    // when(courseRepository.findAll(any(PageRequest.class))).thenReturn(new
    // PageResponse<>(page));

    // PageResponse<Course> response = courseService.findAll(request);

    // assertNotNull(response);
    // verify(courseRepository, times(1)).findAll(any(PageRequest.class));
    // }

    @Test
    void testGetCourseById_NotFound() {
        Integer id = 1;
        when(courseRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> courseService.getCourseById(id));
        verify(courseRepository).findById(id);
        verify(domainEntityNameTranslator).getEntityTranslatedName(Course.class);
    }

    @Test
    void testGetCourseById_Success() {
        Course course = new Course();
        when(courseRepository.findById(1)).thenReturn(Optional.of(course));

        Course result = courseService.getCourseById(1);

        assertNotNull(result);
        verify(courseRepository).findById(1);
    }

    @Test
    void testCreateCourse_SubjectNotActive() {
        Integer subjectId = 1;
        Integer programId = 1;
        
        CourseCreateRequestDto createRequestDto = new CourseCreateRequestDto();
        createRequestDto.setSubjectId(subjectId);
        createRequestDto.setProgramId(programId);

        Program program = new Program();
        Subject subject = new Subject();
        subject.setActive(false);
        subject.setCode("SUB101");
        
        when(programRepository.findById(programId)).thenReturn(Optional.of(program));
        when(subjectRepository.findById(subjectId)).thenReturn(Optional.of(subject));

        assertThrows(SubjectDeactivatedException.class, () -> courseService.createCourse(createRequestDto));
        verify(programRepository).findById(programId);
        verify(subjectRepository).findById(subjectId);
    }

    @Test
    void testUpdateCourse_NotFound() {
        Integer id = 1;
        when(courseRepository.findById(id)).thenReturn(Optional.empty());

        CourseUpdateRequestDto updateRequestDto = new CourseUpdateRequestDto();
        
        assertThrows(ResourceNotFoundException.class, () -> courseService.updateCourse(id, updateRequestDto));
        verify(courseRepository).findById(id);
        verify(domainEntityNameTranslator).getEntityTranslatedName(Course.class);
    }

    @Test
    void testDeleteCourse() {
        Course course = mock(Course.class);

        when(courseRepository.findById(1)).thenReturn(Optional.of(course));
        doNothing().when(courseRepository).deleteById(1);

        courseService.deleteCourse(1);

        verify(courseRepository, times(1)).deleteById(1);
    }
}