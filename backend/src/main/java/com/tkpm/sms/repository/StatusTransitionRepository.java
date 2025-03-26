package com.tkpm.sms.repository;

import com.tkpm.sms.entity.Status;
import com.tkpm.sms.entity.StatusTransition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StatusTransitionRepository extends JpaRepository<StatusTransition, Integer> {
    List<StatusTransition> findByFromStatus(Status fromStatus);
    
    List<StatusTransition> findByToStatus(Status toStatus);
    
    Optional<StatusTransition> findByFromStatusAndToStatus(Status fromStatus, Status toStatus);
    
    boolean existsByFromStatusAndToStatus(Status fromStatus, Status toStatus);
}