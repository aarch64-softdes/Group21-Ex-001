package com.tkpm.sms.repository;

import com.tkpm.sms.entity.Program;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProgramRepository extends JpaRepository<Program, Integer> {
    Optional<Program> findProgramByName(@NotNull(message = "Program's name is required") String name);

    boolean existsProgramByName(@NotNull(message = "Program's name is required") String name);
}
