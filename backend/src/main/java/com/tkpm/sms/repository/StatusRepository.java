package com.tkpm.sms.repository;

import com.tkpm.sms.entity.Status;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StatusRepository extends JpaRepository<Status, Integer> {
    boolean existsStatusByName(@NotNull String name);

    Optional<Status> findStatusByName(@NotNull(message = "Status's name is required") String name);

    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM Status s JOIN StatusTransition t WHERE s.id = :fromStatusId AND t = :toStatusId")
    boolean existsByFromStatusIdAndToStatusId(Integer fromStatusId, Integer toStatusId);
}
