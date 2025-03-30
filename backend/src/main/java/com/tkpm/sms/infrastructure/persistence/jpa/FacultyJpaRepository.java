package com.tkpm.sms.infrastructure.persistence.jpa;

import com.tkpm.sms.infrastructure.persistence.entity.FacultyEntity;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FacultyJpaRepository extends JpaRepository<FacultyEntity, Integer> {
    Optional<FacultyEntity> findFacultyByName(String name);
    boolean existsFacultyByName(String name);

    boolean existsByNameAndIdNot(@NotNull(message = "Faculty's name is required") String name, Integer id);
}