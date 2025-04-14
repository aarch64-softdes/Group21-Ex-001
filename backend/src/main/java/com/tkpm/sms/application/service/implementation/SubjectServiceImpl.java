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
import com.tkpm.sms.domain.exception.SubjectDeletionConstraintException;
import com.tkpm.sms.domain.model.Subject;
import com.tkpm.sms.domain.repository.SubjectRepository;
import com.tkpm.sms.domain.service.validators.SubjectDomainValidator;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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

    @NonFinal
    @Value("${app.constraint.deletable-duration:30}")
    int deletableDuration;

    @Override
    public PageResponse<Subject> findAll(BaseCollectionRequest request) {
        PageRequest pageRequest = PageRequest.from(request);
        return subjectRepository.findAll(pageRequest);
    }

    @Override
    public Subject getSubjectById(Integer id) {
        return subjectRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Subject with id %s not found", id)));
    }

    @Override
    public Subject createSubject(SubjectCreateRequestDto createRequestDto) {
        subjectValidator.validateSubjectCodeUniqueness(createRequestDto.getCode());
        subjectValidator.validateSubjectNameUniqueness(createRequestDto.getName());

        Subject subject = subjectMapper.toSubject(createRequestDto);
        subject.setFaculty(facultyService.getFacultyById(createRequestDto.getFacultyId()));

        subjectValidator.validatePrerequisites(
                createRequestDto.getPrerequisitesId()
        );

        var prerequisites = subjectRepository.findAllByIds(createRequestDto.getPrerequisitesId());
        subject.setPrerequisites(prerequisites);
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

        subjectValidator.validatePrerequisites(
                updateRequestDto.getPrerequisitesId()
        );

        subjectMapper.updateSubjectFromDto(subject, updateRequestDto);

        var prerequisites = subjectRepository.findAllByIds(updateRequestDto.getPrerequisitesId());
        subject.setPrerequisites(prerequisites);

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
            if (timeGap > deletableDuration) {
                throw new SubjectDeletionConstraintException(
                        String.format("Subject with id %s cannot be deleted after 30 minutes of creation. You can deactivate it instead.", id));
            }
        }

        subjectValidator.validateSubjectForDeletion(id);

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
