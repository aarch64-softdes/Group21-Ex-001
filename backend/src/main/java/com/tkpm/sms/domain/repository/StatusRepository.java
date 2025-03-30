package com.tkpm.sms.domain.repository;

import com.tkpm.sms.domain.common.PageRequest;
import com.tkpm.sms.domain.common.PageResponse;
import com.tkpm.sms.domain.model.Status;

import java.util.Optional;

public interface StatusRepository {
    Status save(Status status);
    Optional<Status> findById(Integer id);
    Optional<Status> findByName(String name);
    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, Integer id);
    boolean existsByFromStatusIdAndToStatusId(Integer fromStatusId, Integer toStatusId);
    PageResponse<Status> findAll(PageRequest pageRequest);
}