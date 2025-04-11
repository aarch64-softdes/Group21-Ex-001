package com.tkpm.sms.infrastructure.persistence.jpa;

import com.tkpm.sms.infrastructure.persistence.entity.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseJpaRepository extends JpaRepository<CourseEntity, String> , PagingAndSortingRepository<CourseEntity, String> {
    boolean existsBySchedule(String courseSchedule);

    boolean existsByRoomAndSchedule(String room, String courseSchedule);

    boolean existsByIdNotAndRoomAndSchedule(String id, String room, String schedule);
}
