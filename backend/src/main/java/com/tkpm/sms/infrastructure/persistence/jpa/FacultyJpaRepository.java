package com.tkpm.sms.infrastructure.persistence.jpa;

import com.tkpm.sms.infrastructure.persistence.entity.FacultyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface FacultyJpaRepository extends JpaRepository<FacultyEntity, Integer> {

    @Query("""
                    SELECT f
                    FROM FacultyEntity f
                    JOIN f.name name
                    JOIN name.translations t
                    WHERE t.text = :name OR t.isOriginal = true
            """)
    Optional<FacultyEntity> findFacultyByName(String name);

    @Query("""
                    SELECT COUNT(f) > 0
                    FROM FacultyEntity f
                    JOIN f.name name
                    JOIN name.translations t
                    WHERE t.text = :name OR t.isOriginal = true
            """)
    boolean existsFacultyByName(String name);

    @Query("""
                    SELECT COUNT(f) > 0
                    FROM FacultyEntity f
                    JOIN f.name name
                    JOIN name.translations t
                    WHERE t.text = :name OR t.isOriginal = true AND f.id <> :id
            """)
    boolean existsByNameAndIdNot(String name, Integer id);
}