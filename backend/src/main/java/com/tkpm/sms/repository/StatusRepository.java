package com.tkpm.sms.repository;

import com.tkpm.sms.entity.Status;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StatusRepository extends JpaRepository<Status, Integer> {
    boolean existsStatusByName(@NotNull String name);

    Optional<Status> findStatusByName(@NotNull(message = "Status's name is required") String name);
}
