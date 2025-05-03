package com.tkpm.sms.application.service.implementation;

import com.tkpm.sms.application.dto.request.common.BaseCollectionRequest;
import com.tkpm.sms.application.dto.request.status.StatusRequestDto;
import com.tkpm.sms.application.mapper.StatusMapper;
import com.tkpm.sms.application.service.interfaces.StatusService;
import com.tkpm.sms.domain.common.PageRequest;
import com.tkpm.sms.domain.common.PageResponse;
import com.tkpm.sms.domain.exception.ResourceNotFoundException;
import com.tkpm.sms.domain.model.Status;
import com.tkpm.sms.domain.repository.StatusRepository;
import com.tkpm.sms.domain.service.validators.StatusDomainValidator;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class StatusServiceImpl implements StatusService {
    StatusRepository statusRepository;
    StatusDomainValidator statusDomainValidator;
    StatusMapper statusMapper;

    @Override
    public PageResponse<Status> getAllStatuses(BaseCollectionRequest search) {
        PageRequest pageRequest = PageRequest.from(search);

        return statusRepository.findAll(pageRequest);
    }

    @Override
    public Status getStatusById(Integer id) {
        return statusRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(
                String.format("Status with id %s not found", id)));
    }

    @Override
    public Status getStatusByName(String name) {
        return statusRepository.findByName(name).orElseThrow(() -> new ResourceNotFoundException(
                String.format("Status with name %s not found", name)));
    }

    @Override
    @Transactional
    public Status createStatus(StatusRequestDto statusRequestDto) {
        // Validate name uniqueness
        statusDomainValidator.validateNameUniqueness(statusRequestDto.getName());

        // Map DTO to domain entity
        Status status = statusMapper.toStatus(statusRequestDto);

        // Save and return
        return statusRepository.save(status);
    }

    @Override
    @Transactional
    public Status updateStatus(Integer id, StatusRequestDto statusRequestDto) {
        // Validate name uniqueness for update
        statusDomainValidator.validateNameUniquenessForUpdate(statusRequestDto.getName(), id);

        // Find existing status
        Status status = statusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Status with id %s not found", id)));

        // Update domain entity
        statusMapper.updateStatusFromDto(statusRequestDto, status);

        // Save and return
        return statusRepository.save(status);
    }

    @Override
    @Transactional
    public void deleteStatus(Integer id) {
        statusDomainValidator.validateStatusTransitionForDeletion(id);

        Status status = statusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Status with id %s not found", id)));

        status.setDeletedAt(LocalDate.now());
        statusRepository.save(status);
    }

    @Override
    public boolean isTransitionAllowed(Integer fromStatusId, Integer toStatusId) {
        statusDomainValidator.validateStatusTransition(fromStatusId, toStatusId);
        return true;
    }

    @Override
    public boolean isTransitionAllowed(Status fromStatus, Status toStatus) {
        return fromStatus.canTransitionTo(toStatus);
    }
}