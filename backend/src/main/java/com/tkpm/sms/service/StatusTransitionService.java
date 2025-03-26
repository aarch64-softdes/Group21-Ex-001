package com.tkpm.sms.service;

import com.tkpm.sms.dto.StatusTransitionDto;
import com.tkpm.sms.entity.Status;
import com.tkpm.sms.entity.StatusTransition;
import com.tkpm.sms.repository.StatusRepository;
import com.tkpm.sms.repository.StatusTransitionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatusTransitionService {
    private final StatusTransitionRepository statusTransitionRepository;
    private final StatusRepository statusRepository;

    public List<StatusTransitionDto> getAllTransitions() {
        return statusTransitionRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<StatusTransitionDto> getTransitionsFromStatus(Integer statusId) {
        Status status = statusRepository.findById(statusId)
                .orElseThrow(() -> new EntityNotFoundException("Status not found with ID: " + statusId));
        
        return statusTransitionRepository.findByFromStatus(status)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public StatusTransitionDto getTransition(Integer id) {
        StatusTransition transition = statusTransitionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Status transition not found with ID: " + id));
        
        return convertToDTO(transition);
    }

    @Transactional
    public StatusTransitionDto createTransition(StatusTransitionDto transitionDTO) {
        Status fromStatus = statusRepository.findById(transitionDTO.getFromStatusId())
                .orElseThrow(() -> new EntityNotFoundException("From status not found with ID: " + transitionDTO.getFromStatusId()));
        
        Status toStatus = statusRepository.findById(transitionDTO.getToStatusId())
                .orElseThrow(() -> new EntityNotFoundException("To status not found with ID: " + transitionDTO.getToStatusId()));
        
        if (statusTransitionRepository.existsByFromStatusAndToStatus(fromStatus, toStatus)) {
            throw new IllegalStateException("Transition from status " + fromStatus.getName() + 
                                           " to " + toStatus.getName() + " already exists");
        }
        
        StatusTransition transition = StatusTransition.builder()
                .fromStatus(fromStatus)
                .toStatus(toStatus)
                .build();
        
        StatusTransition savedTransition = statusTransitionRepository.save(transition);
        return convertToDTO(savedTransition);
    }

    @Transactional
    public void deleteTransition(Integer id) {
        if (!statusTransitionRepository.existsById(id)) {
            throw new EntityNotFoundException("Status transition not found with ID: " + id);
        }
        statusTransitionRepository.deleteById(id);
    }

    @Transactional
    public boolean isTransitionAllowed(Integer fromStatusId, Integer toStatusId) {
        Status fromStatus = statusRepository.findById(fromStatusId)
                .orElseThrow(() -> new EntityNotFoundException("From status not found with ID: " + fromStatusId));
        
        Status toStatus = statusRepository.findById(toStatusId)
                .orElseThrow(() -> new EntityNotFoundException("To status not found with ID: " + toStatusId));
        
        return statusTransitionRepository.existsByFromStatusAndToStatus(fromStatus, toStatus);
    }

    private StatusTransitionDto convertToDTO(StatusTransition transition) {
        return StatusTransitionDto.builder()
                .id(transition.getId())
                .fromStatusId(transition.getFromStatus().getId())
                .toStatusId(transition.getToStatus().getId())
                .fromStatusName(transition.getFromStatus().getName())
                .toStatusName(transition.getToStatus().getName())
                .build();
    }
}