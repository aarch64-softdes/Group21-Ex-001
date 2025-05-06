package com.tkpm.sms.infrastructure.persistence.jpa;

import com.tkpm.sms.infrastructure.persistence.entity.ProgramEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProgramJpaRepository extends JpaRepository<ProgramEntity, Integer> {

    @Query("""
                    SELECT p
                    FROM ProgramEntity p
                    JOIN p.name name
                    JOIN name.translations t
                    WHERE t.text = :name OR t.isOriginal = true
            """)
    Optional<ProgramEntity> findProgramByName(String name);

    @Query("""
                    SELECT COUNT(p) > 0
                    FROM ProgramEntity p
                    JOIN p.name name
                    JOIN name.translations t
                    WHERE t.text = :name OR t.isOriginal = true
            """)
    boolean existsProgramByName(String name);

    @Query("""
                    SELECT COUNT(p) > 0
                    FROM ProgramEntity p
                    JOIN p.name name
                    JOIN name.translations t
                    WHERE t.text = :name OR t.isOriginal = true AND p.id <> :id
            """)
    boolean existsProgramByNameAndIdNot(String name, Integer id);
}