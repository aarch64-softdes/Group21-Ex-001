package com.tkpm.sms.infrastructure.persistence.jpa;

import com.tkpm.sms.infrastructure.persistence.entity.StatusEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StatusJpaRepository extends JpaRepository<StatusEntity, Integer> {
    @Query("SELECT CASE WHEN :toStatusId MEMBER OF s.validTransitionIds THEN true ELSE false END FROM StatusEntity s WHERE s.id = :fromStatusId")
    boolean existsByFromStatusIdAndToStatusId(@Param("fromStatusId") Integer fromStatusId,
            @Param("toStatusId") Integer toStatusId);

    @Query("""
                SELECT s FROM StatusEntity s
                JOIN s.name tc
                JOIN tc.translations tr
                WHERE tr.languageCode = :languageCode OR tr.isOriginal = true
            """)
    Page<StatusEntity> findAll(@Param("languageCode") String LanguageCode, Pageable pageable);

    @Query("""
                SELECT COUNT(s) > 0 FROM StatusEntity s
                JOIN s.name tc
                JOIN tc.translations tr
                WHERE (tr.languageCode = :languageCode OR tr.isOriginal = true) AND tr.text = :name
            """)
    boolean existsByName(String name, String languageCode);

    @Query("""
            SELECT COUNT(s) > 0 FROM StatusEntity s
            JOIN s.name tc
            JOIN tc.translations tr
            WHERE tr.languageCode = :languageCode AND tr.text = :name AND s.id <> :id
            """)
    boolean existsByNameAndIdNot(String name, String languageCode, Integer id);
}