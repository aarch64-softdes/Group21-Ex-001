package com.tkpm.sms.domain.service.validators;

import com.tkpm.sms.domain.exception.DuplicateResourceException;
import com.tkpm.sms.domain.model.Program;
import com.tkpm.sms.domain.repository.ProgramRepository;
import com.tkpm.sms.domain.service.DomainEntityNameTranslator;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProgramDomainValidator {
    private final ProgramRepository programRepository;
    private final DomainEntityNameTranslator domainEntityNameTranslator;

    public void validateNameUniqueness(String name) {
        if (programRepository.existsByName(name)) {
            throw new DuplicateResourceException("error.duplicate_resource.name",
                    domainEntityNameTranslator.getEntityTranslatedName(Program.class), name);
        }
    }

    public void validateNameUniquenessForUpdate(String name, Integer id) {
        if (programRepository.existsByNameAndIdNot(name, id)) {
            throw new DuplicateResourceException("error.duplicate_resource.name",
                    domainEntityNameTranslator.getEntityTranslatedName(Program.class), name);
        }
    }
}