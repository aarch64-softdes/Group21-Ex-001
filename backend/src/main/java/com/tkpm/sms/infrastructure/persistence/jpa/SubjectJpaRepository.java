package com.tkpm.sms.infrastructure.persistence.jpa;

import com.tkpm.sms.infrastructure.persistence.entity.SubjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SubjectJpaRepository extends JpaRepository<SubjectEntity, Integer> {
    boolean existsByCode(String code);
    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, Integer id);
    boolean existsByCodeAndIdNot(String code, Integer id);

    @Query("SELECT COUNT(s) > 0 FROM SubjectEntity s JOIN s.prerequisites p WHERE p.id = :subjectId")
    boolean isPrerequisitesForOtherSubjects(Integer subjectId);

    @Query("SELECT COUNT(c) > 0 FROM CourseEntity c WHERE c.subject.id = :subjectId")
    boolean existsCourseForSubject(Integer subjectId);
}
