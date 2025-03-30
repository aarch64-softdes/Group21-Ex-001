package com.tkpm.sms.infrastructure.persistence.jpa;

import com.tkpm.sms.infrastructure.persistence.entity.StatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StatusJpaRepository extends JpaRepository<StatusEntity, Integer> {
    Optional<StatusEntity> findStatusByName(String name);
    boolean existsStatusByName(String name);
    boolean existsStatusByNameAndIdNot(String name, Integer id);

    @Query("SELECT CASE WHEN :toStatusId MEMBER OF s.validTransitionIds THEN true ELSE false END FROM StatusEntity s WHERE s.id = :fromStatusId")
    boolean existsByFromStatusIdAndToStatusId(@Param("fromStatusId") Integer fromStatusId, @Param("toStatusId") Integer toStatusId);
}