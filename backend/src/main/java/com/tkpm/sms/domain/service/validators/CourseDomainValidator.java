package com.tkpm.sms.domain.service.validators;

import com.tkpm.sms.domain.exception.DuplicateResourceException;
import com.tkpm.sms.domain.exception.ResourceNotFoundException;
import com.tkpm.sms.domain.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class CourseDomainValidator {
    private final CourseRepository courseRepository;

    public void validateRoomAndCourseSchedule(String room, String schedule) {
        if (courseRepository.existsByRoomAndCourseSchedule(room, schedule)) {
            throw new DuplicateResourceException(
                    String.format("Course with room %s and schedule %s already exists", room, schedule)
            );
        }
    }

    public void validateRoomAndCourseScheduleForUpdate(Integer id, String room, String schedule) {
        if (courseRepository.existsByIdNotAndRoomAndCourseSchedule(id, room, schedule)) {
            throw new DuplicateResourceException(
                    String.format("Another course with room %s and schedule %s already exists", room, schedule)
            );
        }
    }

    public void validateCodeAndSubject(String code, Integer subjectId) {
        var course = courseRepository.findById(subjectId).orElseThrow(
                () -> new ResourceNotFoundException(
                        String.format("Course with id %s not found", subjectId)
                )
        );

        log.info("Validating course code and subject id: code={}, subjectId={}", code, subjectId);
        if (courseRepository.existsByCodeAndSubjectId(code, subjectId)) {
            throw new DuplicateResourceException(
                    String.format("Course with code %s for subject %s already exists", code, course.getSubject().getCode())
            );
        }
    }

    public void validateCodeAndSubjectForUpdate(Integer id, String code, Integer subjectId) {
        if (courseRepository.existsByCodeAndSubjectIdAndIdNot(code, subjectId, id)) {
            var course = courseRepository.findById(id).orElseThrow(
                    () -> new ResourceNotFoundException(
                            String.format("Course with id %s not found", id)
                    )
            );
            log.info("Validating course code and subject id for update: code={}, subjectCode={}", code, course.getSubject().getCode());
            throw new DuplicateResourceException(
                    String.format("Another course with code %s and subject id %s already exists", code, course.getSubject().getCode())
            );
        }
    }
}
