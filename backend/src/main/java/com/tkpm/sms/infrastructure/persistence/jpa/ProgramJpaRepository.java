package com.tkpm.sms.infrastructure.persistence.jpa;

import com.tkpm.sms.infrastructure.persistence.entity.ProgramEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProgramJpaRepository extends JpaRepository<ProgramEntity, Integer> {
    Optional<ProgramEntity> findProgramByName(String name);
    boolean existsProgramByName(String name);
    boolean existsProgramByNameAndIdNot(String name, Integer id);
}