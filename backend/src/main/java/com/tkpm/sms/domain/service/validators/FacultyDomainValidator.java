package com.tkpm.sms.domain.service.validators;

import com.tkpm.sms.domain.exception.DuplicateResourceException;
import com.tkpm.sms.domain.model.Faculty;
import com.tkpm.sms.domain.repository.FacultyRepository;
import com.tkpm.sms.domain.service.DomainEntityNameTranslator;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FacultyDomainValidator {
    private final FacultyRepository facultyRepository;
    private final DomainEntityNameTranslator domainEntityNameTranslator;

    public void validateNameUniqueness(String name) {
        if (facultyRepository.existsByName(name)) {
            throw new DuplicateResourceException("error.duplicate_resource.name",
                    domainEntityNameTranslator.getEntityTranslatedName(Faculty.class), name);
        }
    }

    public void validateNameUniquenessForUpdate(String name, Integer id) {
        if (facultyRepository.existsByNameAndIdNot(name, id)) {
            throw new DuplicateResourceException("error.duplicate_resource.name",
                    domainEntityNameTranslator.getEntityTranslatedName(Faculty.class), name);
        }
    }
}