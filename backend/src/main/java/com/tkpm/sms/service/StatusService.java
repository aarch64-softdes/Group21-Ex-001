package com.tkpm.sms.service;

import com.tkpm.sms.dto.request.status.StatusRequestDto;
import com.tkpm.sms.dto.request.common.BaseCollectionRequest;
import com.tkpm.sms.entity.Status;
import com.tkpm.sms.exceptions.ApplicationException;
import com.tkpm.sms.exceptions.ErrorCode;
import com.tkpm.sms.repository.StatusRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class StatusService {
    StatusRepository statusRepository;

    public Page<Status> getAllStatuses(BaseCollectionRequest search) {
        Pageable pageable = PageRequest.of(
                search.getPage() - 1,
                search.getSize(),
                Sort.by(
                        search.getSortDirection().equalsIgnoreCase("desc")
                                ? Sort.Direction.DESC
                                : Sort.Direction.ASC,
                        search.getSortBy()
                 ));

        return statusRepository.findAll(pageable);
    }

    public Status getStatus(Integer id) {
        return statusRepository.findById(id).orElseThrow(
                () -> new ApplicationException(
                        ErrorCode.NOT_FOUND.withMessage(
                                String.format("Status with id %s not found", id))));
    }

    public Status getStatusByName(String name) {
        return statusRepository.findStatusByName(name).orElseThrow(
                () -> new ApplicationException(
                        ErrorCode.NOT_FOUND.withMessage(
                                String.format("Status with name %s not found", name))));
    }


    @Transactional
    public Status createStatus(StatusRequestDto status) {
        if (statusRepository.existsStatusByName(status.getName())) {
            throw new ApplicationException(
                    ErrorCode.CONFLICT.withMessage(
                            String.format("Status with name %s already existed", status.getName())));
        }

        var newStatus = Status
                .builder()
                .name(status.getName())
                .validTransitionIds(status.getValidTransitionIds())
                .build();
        var savedStatus = statusRepository.save(newStatus);

        return savedStatus;
    }

    @Transactional
    public Status updateStatus(Integer id, StatusRequestDto status) {
        if (statusRepository.existsStatusByNameAndIdNot(status.getName(), id)) {
            throw new ApplicationException(
                    ErrorCode.CONFLICT.withMessage(
                            String.format("Status with name %s already existed", status.getName())));
        }

        Status statusToUpdate = statusRepository.findById(id).orElseThrow(
                () -> new ApplicationException(
                        ErrorCode.NOT_FOUND.withMessage(
                                String.format("Status with id %s not found", id))));
        statusToUpdate.setName(status.getName());
        statusToUpdate.setValidTransitionIds(status.getValidTransitionIds());

        return statusRepository.save(statusToUpdate);
    }

    @Transactional
    public void deleteStatus(Integer id) {
        Status status = statusRepository.findById(id).orElseThrow(
                () -> new ApplicationException(
                        ErrorCode.NOT_FOUND.withMessage(
                                String.format("Status with id %s not found", id))));
        status.setDeletedAt(LocalDate.now());

        statusRepository.save(status);
    }

    public boolean isTransitionAllowed(Integer fromStatusId, Integer toStatusId) {
        return statusRepository.existsByFromStatusIdAndToStatusId(fromStatusId, toStatusId);
    }
}
