package com.tkpm.sms.application.service.implementation;

import com.tkpm.sms.application.dto.request.common.BaseCollectionRequest;
import com.tkpm.sms.application.dto.request.subject.SubjectCreateRequestDto;
import com.tkpm.sms.application.dto.request.subject.SubjectUpdateRequestDto;
import com.tkpm.sms.application.mapper.SubjectMapper;
import com.tkpm.sms.application.service.interfaces.FacultyService;
import com.tkpm.sms.application.service.interfaces.SubjectService;
import com.tkpm.sms.application.service.interfaces.TextContentService;
import com.tkpm.sms.domain.common.PageRequest;
import com.tkpm.sms.domain.common.PageResponse;
import com.tkpm.sms.domain.exception.ResourceNotFoundException;
import com.tkpm.sms.domain.exception.SubjectDeletionConstraintException;
import com.tkpm.sms.domain.model.Subject;
import com.tkpm.sms.domain.repository.SubjectRepository;
import com.tkpm.sms.domain.service.validators.SubjectDomainValidator;
import com.tkpm.sms.domain.valueobject.TextContent;
import com.tkpm.sms.domain.valueobject.Translation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SubjectServiceImpl implements SubjectService {
    SubjectRepository subjectRepository;
    SubjectDomainValidator subjectValidator;
    SubjectMapper subjectMapper;

    TextContentService textContentService;

    FacultyService facultyService;

    @NonFinal
    @Value("${app.constraint.deletable-duration:30}")
    int deletableDuration;

    @NonFinal
    @Value("${app.locale.default}")
    String DEFAULT_LANGUAGE;

    @Override
    public PageResponse<Subject> findAll(BaseCollectionRequest request) {
        PageRequest pageRequest = PageRequest.from(request);
        return subjectRepository.findAll(pageRequest);
    }

    @Override
    public Subject getSubjectById(Integer id) {
        return subjectRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(
                String.format("Subject with id %s not found", id)));
    }

    @Override
    public Subject createSubject(SubjectCreateRequestDto createRequestDto) {
        subjectValidator.validateSubjectCodeUniqueness(createRequestDto.getCode());
        subjectValidator.validateSubjectNameUniqueness(createRequestDto.getName());

        Subject subject = subjectMapper.toSubject(createRequestDto);
        subject.setFaculty(facultyService.getFacultyById(createRequestDto.getFacultyId()));
        subject.setName(textContentService.createTextContent(createRequestDto.getName()));
        subject.setDescription(textContentService.createTextContent(createRequestDto.getDescription()));

        if (createRequestDto.getPrerequisitesId() == null) {
            createRequestDto.setPrerequisitesId(Collections.emptyList());
        } else {
            subjectValidator.validatePrerequisites(createRequestDto.getPrerequisitesId());

            var prerequisites = subjectRepository
                    .findAllByIds(createRequestDto.getPrerequisitesId());
            subject.setPrerequisites(prerequisites);
        }

        subject.setCreatedAt(LocalDateTime.now());
        subject.setActive(true);

        return subjectRepository.save(subject);
    }

    @Override
    public Subject updateSubject(Integer id, SubjectUpdateRequestDto updateRequestDto) {
        subjectValidator.validateSubjectNameUniquenessForUpdate(updateRequestDto.getName(), id);

        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Subject with id %s not found", id)));

        if(updateRequestDto.getPrerequisitesId() != null) {
            subjectValidator.validatePrerequisites(updateRequestDto.getPrerequisitesId());
            var prerequisites = subjectRepository
                    .findAllByIds(updateRequestDto.getPrerequisitesId());
            subject.setPrerequisites(prerequisites);
        }

        subjectMapper.updateSubjectFromDto(subject, updateRequestDto);
        subject.setName(
                textContentService.updateTextContent(subject.getName(), updateRequestDto.getName()));
        subject.setDescription(
                textContentService.updateTextContent(subject.getDescription(), updateRequestDto.getDescription()));

        return subjectRepository.save(subject);
    }

    @Override
    public void deleteSubject(Integer id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Subject with id %s not found", id)));

        // Add time constraint check
        var createdAt = subject.getCreatedAt();
        if (createdAt != null) {
            var timeGap = ChronoUnit.MINUTES.between(createdAt, LocalDateTime.now());
            // in minutes
            if (timeGap > deletableDuration) {
                throw new SubjectDeletionConstraintException(
                        "Target subject cannot be deleted after 30 minutes of creation. You can deactivate it instead.");
            }
        }

        subjectValidator.validateSubjectForDeletionAndDeactivation(id);

        subjectRepository.delete(subject);
    }

    @Override
    public void deactivateSubject(Integer id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Subject with id %s not found", id)));

        subjectValidator.validateSubjectForDeletionAndDeactivation(id);

        subject.setActive(false);

        subjectRepository.save(subject);
    }

    @Override
    public void activateSubject(Integer id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Subject with id %s not found", id)));

        subject.setActive(true);

        subjectRepository.save(subject);
    }
}
