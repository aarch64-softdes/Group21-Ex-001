package com.tkpm.sms.domain.service.validators;

import com.tkpm.sms.domain.exception.DuplicateResourceException;
import com.tkpm.sms.domain.repository.FacultyRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FacultyDomainValidator {
    private final FacultyRepository facultyRepository;

    public void validateNameUniqueness(String name) {
        if (facultyRepository.existsByName(name)) {
            throw new DuplicateResourceException(
                    String.format("Faculty with name %s already exists", name)
            );
        }
    }

    public void validateNameUniquenessForUpdate(String name, Integer id) {
        if (facultyRepository.existsByNameAndIdNot(name, id)) {
            throw new DuplicateResourceException(
                    String.format("Faculty with name %s already exists", name)
            );
        }
    }
}