package com.tkpm.sms.infrastructure.persistence.jpa;

import com.tkpm.sms.infrastructure.persistence.entity.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseJpaRepository
        extends
            JpaRepository<CourseEntity, Integer>,
            PagingAndSortingRepository<CourseEntity, Integer> {

    boolean existsByCodeAndSubjectId(String code, Integer subjectId);

    boolean existsByCodeAndSubjectIdAndIdNot(String code, Integer subjectId, Integer id);

    List<CourseEntity> findAllByRoom(String room);

    List<CourseEntity> findAllBySemesterAndYearAndRoom(int semester, int year, String room);

    List<CourseEntity> findAllBySubjectId(Integer subjectId);
}
