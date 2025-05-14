package com.tkpm.sms.infrastructure.persistence.repository;

import com.tkpm.sms.domain.common.PageRequest;
import com.tkpm.sms.domain.common.PageResponse;
import com.tkpm.sms.domain.exception.ResourceNotFoundException;
import com.tkpm.sms.domain.model.Course;
import com.tkpm.sms.domain.repository.CourseRepository;
import com.tkpm.sms.infrastructure.persistence.entity.CourseEntity;
import com.tkpm.sms.infrastructure.persistence.jpa.CourseJpaRepository;
import com.tkpm.sms.infrastructure.persistence.mapper.CoursePersistenceMapper;
import com.tkpm.sms.infrastructure.utils.PagingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CourseRepositoryImpl implements CourseRepository {

    private final CourseJpaRepository courseJpaRepository;
    private final CoursePersistenceMapper coursePersistenceMapper;

    @Override
    public PageResponse<Course> findAll(PageRequest request) {
        // Convert domain PageRequest to Spring Pageable
        Pageable pageable = PagingUtils.toSpringPageable(request);

        Page<CourseEntity> page = courseJpaRepository.findAll(pageable);

        // Convert Spring Page to domain PageResponse
        List<Course> courses = page.getContent().stream().map(coursePersistenceMapper::toDomain)
                .collect(Collectors.toList());

        return PageResponse.of(courses, page.getNumber() + 1, // Convert 0-based to 1-based
                page.getSize(), page.getTotalElements(), page.getTotalPages());
    }

    @Override
    public Optional<Course> findById(Integer id) {
        return courseJpaRepository.findById(id).map(coursePersistenceMapper::toDomain);
    }

    @Override
    public Course save(Course course) {
        return coursePersistenceMapper
                .toDomain(courseJpaRepository.save(coursePersistenceMapper.toEntity(course)));
    }

    @Override
    public void deleteById(Integer id) {
        var course = courseJpaRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Course not found with id: " + id));

        courseJpaRepository.delete(course);
    }

    @Override
    public List<Course> findAllWithSameRoom(int semester, int year, String room) {
        return courseJpaRepository.findAllBySemesterAndYearAndRoom(semester, year, room).stream()
                .map(coursePersistenceMapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public boolean existsByCodeAndSubjectId(String code, Integer subjectId) {
        return courseJpaRepository.existsByCodeAndSubjectId(code, subjectId);
    }

    @Override
    public boolean existsByCodeAndSubjectIdAndIdNot(String code, Integer subjectId, Integer id) {
        return courseJpaRepository.existsByCodeAndSubjectIdAndIdNot(code, subjectId, id);
    }

    @Override
    public boolean existsBySubjectIdAndIdNot(Integer subjectId, Integer id) {
        return courseJpaRepository.existsBySubjectIdAndIdNot(subjectId, id);
    }
}
