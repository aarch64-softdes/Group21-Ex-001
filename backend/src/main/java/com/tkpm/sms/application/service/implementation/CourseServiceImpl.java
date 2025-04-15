package com.tkpm.sms.application.service.implementation;

import com.tkpm.sms.application.dto.request.common.BaseCollectionRequest;
import com.tkpm.sms.application.dto.request.course.CourseCreateRequestDto;
import com.tkpm.sms.application.dto.request.course.CourseUpdateRequestDto;
import com.tkpm.sms.application.mapper.CourseMapper;
import com.tkpm.sms.application.service.interfaces.CourseService;
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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    @Override
    public PageResponse<Course> findAll(BaseCollectionRequest request) {
        Sort.Direction direction = Sort.Direction.valueOf(request.getSortDirection().toUpperCase());

        return courseRepository.findAll(
                PageRequest.of(
                        request.getPage() - 1,
                        request.getSize(),
                        Sort.by(direction, request.getSortBy())
                )
        );
    }

    @Override
    public Course getCourseById(Integer id) {
        var course = courseRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(
                        String.format("Course with id %s not found", id)
                )
        );
        return course;
    }

    @Override
    @Transactional
    public Course createCourse(CourseCreateRequestDto createRequestDto) {
        Program program = programRepository.findById(createRequestDto.getProgramId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Program with id " + createRequestDto.getProgramId() + " not found"
                ));

        Subject subject = subjectRepository.findById(createRequestDto.getSubjectId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Subject with id " + createRequestDto.getSubjectId() + " not found"
                ));

        if (!subject.isActive()) {
            throw new SubjectDeactivatedException(
                    "Subject with id " + createRequestDto.getSubjectId() + " is not active"
            );
        }

        // Map DTO to domain object (excluding foreign keys)
        Course course = courseMapper.toDomain(createRequestDto);

        // Set foreign key relationships
        course.setProgram(program);
        course.setSubject(subject);

        courseValidator.validateRoomAndCourseSchedule(course.getRoom(), course.getSchedule());
        courseValidator.validateCodeAndSubject(course.getCode(), subject.getId());

        return courseRepository.save(course);
    }

    @Override
    @Transactional
    public Course updateCourse(Integer id, CourseUpdateRequestDto updateRequestDto) {
        var course = courseRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(
                String.format("Course with id %s not found", id)
        ));

        courseValidator.validateRoomAndCourseScheduleForUpdate(
                id,
                updateRequestDto.getRoom(),
                updateRequestDto.getSchedule().toString()
        );
        courseValidator.validateCodeAndSubjectForUpdate(
                id,
                updateRequestDto.getCode(),
                course.getSubject().getId()
        );

        courseMapper.toDomain(course, updateRequestDto);

        return courseRepository.save(course);
    }

    @Override
    public void deleteCourse(Integer id) {
        courseRepository.deleteById(id);
    }
}
