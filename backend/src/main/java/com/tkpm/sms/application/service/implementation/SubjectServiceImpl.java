package com.tkpm.sms.application.service.implementation;

import com.tkpm.sms.application.dto.request.common.BaseCollectionRequest;
import com.tkpm.sms.application.dto.request.subject.SubjectCreateRequestDto;
import com.tkpm.sms.application.dto.request.subject.SubjectUpdateRequestDto;
import com.tkpm.sms.application.mapper.SubjectMapper;
import com.tkpm.sms.application.service.interfaces.FacultyService;
import com.tkpm.sms.application.service.interfaces.SubjectService;
import com.tkpm.sms.domain.common.PageRequest;
import com.tkpm.sms.domain.common.PageResponse;
import com.tkpm.sms.domain.exception.ResourceNotFoundException;
import com.tkpm.sms.domain.exception.SubjectDeletionTimeConstraintException;
import com.tkpm.sms.domain.model.Subject;
import com.tkpm.sms.domain.repository.SubjectRepository;
import com.tkpm.sms.domain.service.validators.SubjectDomainValidator;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SubjectServiceImpl implements SubjectService {
    SubjectRepository subjectRepository;
    SubjectDomainValidator subjectValidator;
    SubjectMapper subjectMapper;

    FacultyService facultyService;

    @Override
    public PageResponse<Subject> findAll(BaseCollectionRequest request) {
        PageRequest pageRequest = PageRequest.builder()
                .pageNumber(request.getPage())
                .pageSize(request.getSize())
                .sortBy(request.getSortBy())
                .sortDirection("asc".equalsIgnoreCase(request.getSortDirection())
                        ? PageRequest.SortDirection.ASC
                        : PageRequest.SortDirection.DESC)
                .build();

        return subjectRepository.findAll(pageRequest);
    }

    @Override
    public Subject getSubjectById(Integer id) {
        return subjectRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Subject with id %s not found", id)));
    }

    @Override
    public Subject createSubject(SubjectCreateRequestDto subjectRequestDto) {
        subjectValidator.validateSubjectCodeUniqueness(subjectRequestDto.getCode());
        subjectValidator.validateSubjectNameUniqueness(subjectRequestDto.getName());

        Subject subject = subjectMapper.toSubject(subjectRequestDto);
        subject.setFaculty(facultyService.getFacultyById(subjectRequestDto.getFacultyId()));
        subject.setPrerequisites(
                subjectRequestDto.getPrerequisitesId().stream()
                        .map(
                                subjectId -> subjectRepository.findById(subjectId)
                                        .orElseThrow(() -> new ResourceNotFoundException(
                                                String.format("Subject with id %s not found", subjectId)))
                        )
                        .toList()
        );
        subject.setCreatedAt(LocalDateTime.now());
        subject.setActive(true);

        return subjectRepository.save(subject);
    }

    @Override
    public Subject updateSubject(Integer id, SubjectUpdateRequestDto updateRequestDto) {
        subjectValidator.validateSubjectNameUniquenessForUpdate(updateRequestDto.getName(), id);

        Subject subject = subjectRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Subject with id %s not found", id)));


        subjectMapper.updateSubjectFromDto(subject, updateRequestDto);

        return subjectRepository.save(subject);
    }

    @Override
    public void deleteSubject(Integer id) {
        Subject subject = subjectRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Subject with id %s not found", id)));

        // Add time constraint check
        var createdAt = subject.getCreatedAt();
        if (createdAt != null) {
            var timeGap = ChronoUnit.MINUTES.between(createdAt, LocalDateTime.now());
            // in minutes
            int MINIMUM_TIME_GAP_FOR_SUBJECT_TOBE_DELETED = 30;
            if (timeGap > MINIMUM_TIME_GAP_FOR_SUBJECT_TOBE_DELETED) {
                throw new SubjectDeletionTimeConstraintException(
                        String.format("Subject with id %s cannot be deleted after 30 minutes of creation. You can deactivate it instead.", id));
            }
        }

        subjectValidator.validateSubjectIsPrerequisiteForOtherSubjects(id);

        // TODO: Check if there is any course which is opened with this subject
        // Wait for PR CRUD Course to be merged.

        subjectRepository.delete(subject);
    }

    @Override
    public void deactivateSubject(Integer id) {
        Subject subject = subjectRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Subject with id %s not found", id)));

        subject.setActive(false);

        subjectRepository.save(subject);
    }

    @Override
    public void activateSubject(Integer id) {
        Subject subject = subjectRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Subject with id %s not found", id)));

        subject.setActive(true);

        subjectRepository.save(subject);
    }
}
