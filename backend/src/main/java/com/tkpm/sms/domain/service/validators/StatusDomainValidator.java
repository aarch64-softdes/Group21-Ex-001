package com.tkpm.sms.domain.service.validators;

import com.tkpm.sms.domain.exception.DuplicateResourceException;
import com.tkpm.sms.domain.exception.UnsupportedStatusTransitionException;
import com.tkpm.sms.domain.model.Status;
import com.tkpm.sms.domain.repository.StatusRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class StatusDomainValidator {
    private final StatusRepository statusRepository;

    public void validateNameUniqueness(String name) {
        if (statusRepository.existsByName(name)) {
            throw new DuplicateResourceException(
                    String.format("Status with name %s already exists", name));
        }
    }

    public void validateNameUniquenessForUpdate(String name, Integer id) {
        if (statusRepository.existsByNameAndIdNot(name, id)) {
            throw new DuplicateResourceException(
                    String.format("Status with name %s already exists", name));
        }
    }

    public void validateStatusTransition(Integer fromStatusId, Integer toStatusId) {
        if (!statusRepository.existsByFromStatusIdAndToStatusId(fromStatusId, toStatusId)) {
            throw new UnsupportedStatusTransitionException(
                    String.format("Transition from status ID %d to status ID %d is not allowed",
                            fromStatusId, toStatusId));
        }
    }

    public void validateStatusTransitionForDeletion(Integer id) {
        List<Status> statuses = statusRepository.findAllStatusesHaveThisAsTransition(id);
        var status = statusRepository.findById(id)
                .orElseThrow(() -> new UnsupportedStatusTransitionException(
                        String.format("Status with id %s not found", id)));

        if (!statuses.isEmpty()) {
            throw new UnsupportedStatusTransitionException(String.format(
                    "Cannot delete status %s as it is involved in a transition to status: %s",
                    status.getName(),
                    String.join(", ", statuses.stream().map(Status::getName).toList())));
        }
    }
}