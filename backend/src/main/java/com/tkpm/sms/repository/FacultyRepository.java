package com.tkpm.sms.repository;

import com.tkpm.sms.entity.Faculty;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FacultyRepository extends JpaRepository<Faculty, Integer> {
    Optional<Faculty> findFacultyByName(String name);

    boolean existsFacultyByName(@NotNull(message = "Faculty's name is required") String name);
}
