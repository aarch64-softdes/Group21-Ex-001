package com.tkpm.sms.infrastructure.persistence.jpa;

import com.tkpm.sms.infrastructure.persistence.entity.SubjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SubjectJpaRepository extends JpaRepository<SubjectEntity, Integer> {

    boolean existsByCode(String code);

    @Query("""
                  SELECT Count(s) > 0
                  FROM SubjectEntity s
                  JOIN s.name name
                  JOIN name.translations t
                  WHERE t.text = :name AND t.languageCode = :languageCode
            """)
    boolean existsByName(String name, String languageCode);

    @Query("""
                  SELECT Count(s) > 0
                  FROM SubjectEntity s
                  JOIN s.name name
                  JOIN name.translations t
                  WHERE (t.text = :name AND t.languageCode = :languageCode)
                        AND s.id <> :id
            """)
    boolean existsByNameAndIdNot(String name, Integer id, String languageCode);

    boolean existsByCodeAndIdNot(String code, Integer id);

    @Query("SELECT COUNT(s) > 0 FROM SubjectEntity s JOIN s.prerequisites p WHERE p.id = :subjectId")
    boolean isPrerequisitesForOtherSubjects(Integer subjectId);

    @Query("SELECT COUNT(c) > 0 FROM CourseEntity c WHERE c.subject.id = :subjectId")
    boolean existsCourseForSubject(Integer subjectId);
}
