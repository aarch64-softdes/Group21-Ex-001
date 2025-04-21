package com.tkpm.sms.infrastructure.persistence.jpa;

import com.tkpm.sms.infrastructure.persistence.entity.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseJpaRepository
        extends
            JpaRepository<CourseEntity, Integer>,
            PagingAndSortingRepository<CourseEntity, Integer> {
    boolean existsByRoomAndSchedule(String room, String courseSchedule);

    boolean existsByIdNotAndRoomAndSchedule(Integer id, String room, String schedule);

    boolean existsByCodeAndSubjectId(String code, Integer subjectId);

    boolean existsByCodeAndSubjectIdAndIdNot(String code, Integer subjectId, Integer id);
}
