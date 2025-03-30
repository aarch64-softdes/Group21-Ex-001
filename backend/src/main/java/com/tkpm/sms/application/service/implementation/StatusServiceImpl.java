// application/service/StatusServiceImpl.java
package com.tkpm.sms.application.service.implementation;

import com.tkpm.sms.application.dto.request.common.BaseCollectionRequest;
import com.tkpm.sms.application.dto.request.status.StatusRequestDto;
import com.tkpm.sms.application.exception.ApplicationException;
import com.tkpm.sms.application.exception.ErrorCode;
import com.tkpm.sms.application.exception.ExceptionTranslator;
import com.tkpm.sms.application.mapper.PageRequestMapper;
import com.tkpm.sms.application.mapper.StatusMapper;
import com.tkpm.sms.application.service.interfaces.StatusService;
import com.tkpm.sms.domain.common.PageRequest;
import com.tkpm.sms.domain.common.PageResponse;
import com.tkpm.sms.domain.model.Status;
import com.tkpm.sms.domain.exception.DomainException;
import com.tkpm.sms.domain.exception.ResourceNotFoundException;
import com.tkpm.sms.domain.repository.StatusRepository;
import com.tkpm.sms.domain.service.validators.StatusValidator;
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
    StatusValidator statusDomainService;
    StatusMapper statusMapper;
    ExceptionTranslator exceptionTranslator;

    @Override
    public PageResponse<Status> getAllStatuses(BaseCollectionRequest search) {
        PageRequest pageRequest = PageRequest.builder()
                .pageNumber(search.getPage())
                .pageSize(search.getSize())
                .sortBy(search.getSortBy())
                .sortDirection("desc".equalsIgnoreCase(search.getSortDirection())
                        ? PageRequest.SortDirection.DESC
                        : PageRequest.SortDirection.ASC)
                .build();

        return statusRepository.findAll(pageRequest);
    }

    @Override
    public Status getStatusById(Integer id) {
        return statusRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(
                        ErrorCode.NOT_FOUND.withMessage(
                                String.format("Status with id %s not found", id)
                        )));
    }

    @Override
    public Status getStatusByName(String name) {
        return statusRepository.findByName(name)
                .orElseThrow(() -> new ApplicationException(
                        ErrorCode.NOT_FOUND.withMessage(
                                String.format("Status with name %s not found", name)
                        )));
    }

    @Override
    @Transactional
    public Status createStatus(StatusRequestDto statusRequestDto) {
        try {
            // Validate name uniqueness
            statusDomainService.validateNameUniqueness(statusRequestDto.getName());

            // Map DTO to domain entity
            Status status = statusMapper.toStatus(statusRequestDto);

            // Save and return
            return statusRepository.save(status);
        } catch (DomainException e) {
            throw exceptionTranslator.translateException(e);
        }
    }

    @Override
    @Transactional
    public Status updateStatus(Integer id, StatusRequestDto statusRequestDto) {
        try {
            // Validate name uniqueness for update
            statusDomainService.validateNameUniquenessForUpdate(statusRequestDto.getName(), id);

            // Find existing status
            Status status = statusRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            String.format("Status with id %s not found", id)));

            // Update domain entity
            statusMapper.updateStatusFromDto(statusRequestDto, status);

            // Save and return
            return statusRepository.save(status);
        } catch (DomainException e) {
            throw exceptionTranslator.translateException(e);
        }
    }

    @Override
    @Transactional
    public void deleteStatus(Integer id) {
        try {
            Status status = statusRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            String.format("Status with id %s not found", id)));

            status.setDeletedAt(LocalDate.now());
            statusRepository.save(status);
        } catch (DomainException e) {
            throw exceptionTranslator.translateException(e);
        }
    }

    @Override
    public boolean isTransitionAllowed(Integer fromStatusId, Integer toStatusId) {
        try {
            statusDomainService.validateStatusTransition(fromStatusId, toStatusId);
            return true;
        } catch (DomainException e) {
            return false;
        }
    }

    @Override
    public boolean isTransitionAllowed(Status fromStatus, Status toStatus) {
        return fromStatus.canTransitionTo(toStatus);
    }
}