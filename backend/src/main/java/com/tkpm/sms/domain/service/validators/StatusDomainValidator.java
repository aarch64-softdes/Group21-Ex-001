package com.tkpm.sms.domain.service.validators;

import com.tkpm.sms.domain.exception.DuplicateResourceException;
import com.tkpm.sms.domain.exception.UnsupportedStatusTransitionException;
import com.tkpm.sms.domain.repository.StatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
public class StatusDomainValidator {
    private final StatusRepository statusRepository;

    public void validateNameUniqueness(String name) {
        if (statusRepository.existsByName(name)) {
            throw new DuplicateResourceException(
                    String.format("Status with name %s already exists", name)
            );
        }
    }

    public void validateNameUniquenessForUpdate(String name, Integer id) {
        if (statusRepository.existsByNameAndIdNot(name, id)) {
            throw new DuplicateResourceException(
                    String.format("Status with name %s already exists", name)
            );
        }
    }

    public void validateStatusTransition(Integer fromStatusId, Integer toStatusId) {
        if (!statusRepository.existsByFromStatusIdAndToStatusId(fromStatusId, toStatusId)) {
            throw new UnsupportedStatusTransitionException(
                    String.format("Transition from status ID %d to status ID %d is not allowed",
                            fromStatusId, toStatusId)
            );
        }
    }
}