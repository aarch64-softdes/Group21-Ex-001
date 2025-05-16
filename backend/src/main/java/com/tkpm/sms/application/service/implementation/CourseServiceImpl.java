package com.tkpm.sms.application.service.implementation;

import com.tkpm.sms.application.dto.request.common.BaseCollectionRequest;
import com.tkpm.sms.application.dto.request.course.CourseCreateRequestDto;
import com.tkpm.sms.application.dto.request.course.CourseUpdateRequestDto;
import com.tkpm.sms.application.mapper.CourseMapper;
import com.tkpm.sms.application.service.interfaces.CourseService;
import com.tkpm.sms.domain.common.PageRequest;
import com.tkpm.sms.domain.common.PageResponse;
import com.tkpm.sms.domain.exception.ResourceNotFoundException;
import com.tkpm.sms.domain.exception.SubjectDeactivatedException;
import com.tkpm.sms.domain.model.Course;
import com.tkpm.sms.domain.model.Program;
import com.tkpm.sms.domain.model.Subject;
import com.tkpm.sms.domain.repository.CourseRepository;
import com.tkpm.sms.domain.repository.ProgramRepository;
import com.tkpm.sms.domain.repository.SubjectRepository;
import com.tkpm.sms.domain.service.TranslatorService;
import com.tkpm.sms.domain.service.validators.CourseDomainValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class CourseServiceImpl implements CourseService {
    CourseMapper courseMapper;

    CourseRepository courseRepository;
    ProgramRepository programRepository;
    SubjectRepository subjectRepository;

    CourseDomainValidator courseValidator;

    TranslatorService translatorService;

    @Override
    public PageResponse<Course> findAll(BaseCollectionRequest request) {
        PageRequest pageRequest = PageRequest.from(request);
        return courseRepository.findAll(pageRequest);
    }

    @Override
    public Course getCourseById(Integer id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("error.not_found.id",
                        translatorService.getEntityTranslatedName(Course.class), id));
    }

    @Override
    @Transactional
    public Course createCourse(CourseCreateRequestDto createRequestDto) {
        Program program = programRepository.findById(createRequestDto.getProgramId())
                .orElseThrow(() -> new ResourceNotFoundException("error.not_found.id",
                        translatorService.getEntityTranslatedName(Program.class),
                        createRequestDto.getProgramId()));

        Subject subject = subjectRepository.findById(createRequestDto.getSubjectId())
                .orElseThrow(() -> new ResourceNotFoundException("error.not_found.id",
                        translatorService.getEntityTranslatedName(Subject.class),
                        createRequestDto.getSubjectId()));

        if (!subject.isActive()) {
            throw new SubjectDeactivatedException("error.subject.is_deactivated",
                    translatorService.getEntityTranslatedName(Subject.class), subject.getCode());
        }

        // Map DTO to domain object (excluding foreign keys)
        Course course = courseMapper.toDomain(createRequestDto);

        // Set foreign key relationships
        course.setProgram(program);
        course.setSubject(subject);

        courseValidator.validateRoomAndCourseSchedule(course);
        courseValidator.validateCodeAndSubject(course.getCode(), subject.getId());

        return courseRepository.save(course);
    }

    @Override
    @Transactional
    public Course updateCourse(Integer id, CourseUpdateRequestDto updateRequestDto) {
        var course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("error.not_found.id",
                        translatorService.getEntityTranslatedName(Course.class), id));

        courseMapper.toDomain(course, updateRequestDto);

        courseValidator.validateRoomAndCourseSchedule(course);
        courseValidator.validateSubjectForCourseUpdate(id, course.getSubject().getId());

        return courseRepository.save(course);
    }

    @Override
    public void deleteCourse(Integer id) {
        var course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("error.not_found.id",
                        translatorService.getEntityTranslatedName(Course.class), id));

        courseValidator.validateCourseInTimePeriod(course);
        courseValidator.validateEnrollmentExistenceForCourse(course);

        courseRepository.deleteById(id);
    }
}
