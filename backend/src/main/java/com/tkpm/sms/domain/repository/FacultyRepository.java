package com.tkpm.sms.domain.repository;

import com.tkpm.sms.domain.common.PageRequest;
import com.tkpm.sms.domain.common.PageResponse;
import com.tkpm.sms.domain.model.Faculty;
import java.util.Optional;

public interface FacultyRepository {
    Faculty save(Faculty faculty);

    Optional<Faculty> findById(Integer id);

    Optional<Faculty> findByName(String name);

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Integer id);

    void deleteById(Integer id);

    PageResponse<Faculty> findAll(PageRequest pageRequest);
}