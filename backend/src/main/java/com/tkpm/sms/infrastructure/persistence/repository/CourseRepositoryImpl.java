package com.tkpm.sms.infrastructure.persistence.repository;

import com.tkpm.sms.domain.common.PageResponse;
import com.tkpm.sms.domain.exception.ResourceNotFoundException;
import com.tkpm.sms.domain.model.Course;
import com.tkpm.sms.domain.repository.CourseRepository;
import com.tkpm.sms.infrastructure.persistence.jpa.CourseJpaRepository;
import com.tkpm.sms.infrastructure.persistence.mapper.CoursePersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CourseRepositoryImpl implements CourseRepository {

    private final CourseJpaRepository courseJpaRepository;
    private final CoursePersistenceMapper coursePersistenceMapper;

    @Override
    public PageResponse<Course> findAll(PageRequest pageRequest) {
        var courses = courseJpaRepository.findAll(pageRequest).stream()
                .map(coursePersistenceMapper::toDomain)
                .toList();

        return PageResponse.of(
                courses,
                courses.size(),
                pageRequest.getPageNumber(),
                pageRequest.getPageSize(),
                courses.size() / pageRequest.getPageSize()
        );
    }

    @Override
    public Optional<Course> findById(Integer id) {
        return courseJpaRepository.findById(id)
                .map(coursePersistenceMapper::toDomain);
    }

    @Override
    public Course save(Course course) {
        return coursePersistenceMapper.toDomain(
                courseJpaRepository.save(coursePersistenceMapper.toEntity(course))
        );
    }

    @Override
    public void deleteById(Integer id) {
        var course = courseJpaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(
                "Course not found with id: " + id
        ));

        courseJpaRepository.delete(course);
    }

    @Override
    public boolean existsByRoomAndCourseSchedule(String room, String schedule) {
        return courseJpaRepository.existsByRoomAndSchedule(room, schedule);
    }

    @Override
    public boolean existsByIdNotAndRoomAndCourseSchedule(Integer id, String room, String schedule) {
        return courseJpaRepository.existsByIdNotAndRoomAndSchedule(id, room, schedule);
    }

    @Override
    public boolean existsByCodeAndSubjectId(String code, Integer subjectId) {
        return courseJpaRepository.existsByCodeAndSubjectId(code, subjectId);
    }

    @Override
    public boolean existsByCodeAndSubjectIdAndIdNot(String code, Integer subjectId, Integer id) {
        return courseJpaRepository.existsByCodeAndSubjectIdAndIdNot(code, subjectId, id);
    }
}
