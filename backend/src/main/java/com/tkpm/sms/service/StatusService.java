package com.tkpm.sms.service;

import com.tkpm.sms.dto.request.StatusRequestDto;
import com.tkpm.sms.entity.Status;
import com.tkpm.sms.exceptions.ApplicationException;
import com.tkpm.sms.exceptions.ErrorCode;
import com.tkpm.sms.repository.StatusRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class StatusService {
    StatusRepository statusRepository;

    public List<Status> getAllStatuses() {
        return statusRepository.findAll();
    }

    public Status getStatus(Integer id) {
        return statusRepository.findById(id).orElseThrow(
                () -> new ApplicationException(ErrorCode.NOT_FOUND.withMessage(String.format("Status with id %s not found", id))));
    }

    public Status getStatusByName(String name) {
        return statusRepository.findStatusByName(name.toLowerCase()).orElseThrow(
                () -> new ApplicationException(ErrorCode.NOT_FOUND.withMessage(String.format("Status with name %s not found", name))));
    }

    public Status createStatus(StatusRequestDto status) {
        if(statusRepository.existsStatusByName(status.getName())) {
            throw new ApplicationException(ErrorCode.CONFLICT.withMessage(String.format("Status with name %s already existed", status.getName())));
        }
        var newStatus = Status.builder().name(status.getName().toLowerCase()).build();

        return statusRepository.save(newStatus);
    }

    public Status updateStatus(Integer id, StatusRequestDto status) {
        if(statusRepository.existsStatusByName(status.getName())) {
            throw new ApplicationException(ErrorCode.CONFLICT.withMessage(String.format("Status with name %s already existed", status.getName())));
        }

        Status statusToUpdate = statusRepository.findById(id).orElseThrow(
                () -> new ApplicationException(ErrorCode.NOT_FOUND.withMessage(String.format("Status with id %s not found", id))));
        statusToUpdate.setName(status.getName().toLowerCase());
        return statusRepository.save(statusToUpdate);
    }

    public void deleteStatus(Integer id) {
        Status status = statusRepository.findById(id).orElseThrow(
                () -> new ApplicationException(ErrorCode.NOT_FOUND.withMessage(String.format("Status with id %s not found", id))));
        status.setDeletedAt(LocalDate.now());
        statusRepository.save(status);
    }
}
