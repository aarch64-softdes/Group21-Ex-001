package com.tkpm.sms.repository;

import com.tkpm.sms.entity.Status;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StatusRepository extends JpaRepository<Status, Integer> {
    boolean existsStatusByName(@NotNull String name);

    Optional<Status> findStatusByName(@NotNull(message = "Status's name is required") String name);

   @Query("SELECT CASE WHEN :toStatusId MEMBER OF s.validTransitionIds THEN true ELSE false END FROM Status s WHERE s.id = :fromStatusId")
   boolean existsByFromStatusIdAndToStatusId(Integer fromStatusId, Integer toStatusId);

    boolean existsStatusByNameAndIdNot(@NotNull(message = "Status's name is required") String name, Integer id);
}
