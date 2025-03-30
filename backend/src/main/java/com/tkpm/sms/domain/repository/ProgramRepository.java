package com.tkpm.sms.domain.repository;

import com.tkpm.sms.domain.common.PageRequest;
import com.tkpm.sms.domain.common.PageResponse;
import com.tkpm.sms.domain.model.Program;

import java.util.Optional;

public interface ProgramRepository {
    Program save(Program program);
    Optional<Program> findById(Integer id);
    Optional<Program> findByName(String name);
    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, Integer id);
    PageResponse<Program> findAll(PageRequest pageRequest);
}