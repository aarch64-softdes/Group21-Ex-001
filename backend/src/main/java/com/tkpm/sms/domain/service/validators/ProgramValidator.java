package com.tkpm.sms.domain.service.validators;

import com.tkpm.sms.domain.exception.DuplicateResourceException;
import com.tkpm.sms.domain.repository.ProgramRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProgramValidator {
    private final ProgramRepository programRepository;

    public void validateNameUniqueness(String name) {
        if (programRepository.existsByName(name)) {
            throw new DuplicateResourceException(
                    String.format("Program with name %s already exists", name)
            );
        }
    }

    public void validateNameUniquenessForUpdate(String name, Integer id) {
        if (programRepository.existsByNameAndIdNot(name, id)) {
            throw new DuplicateResourceException(
                    String.format("Program with name %s already exists", name)
            );
        }
    }
}