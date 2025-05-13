package com.tkpm.sms.domain.service.validators;

import com.tkpm.sms.domain.exception.DuplicateResourceException;
import com.tkpm.sms.domain.exception.ResourceNotFoundException;
import com.tkpm.sms.domain.exception.SubjectDeactivatedException;
import com.tkpm.sms.domain.exception.SubjectDeletionConstraintException;
import com.tkpm.sms.domain.model.Course;
import com.tkpm.sms.domain.model.Subject;
import com.tkpm.sms.domain.repository.CourseRepository;
import com.tkpm.sms.domain.repository.SubjectRepository;
import com.tkpm.sms.domain.service.TranslatorService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class SubjectDomainValidator {
    private final SubjectRepository subjectRepository;
    private final TranslatorService translatorService;

    public void validateSubjectCodeUniqueness(String code) {
        if (subjectRepository.existsByCode(code)) {
            throw new DuplicateResourceException("error.subject.duplicate_resource.code",
                    translatorService.getEntityTranslatedName(Subject.class), code);
        }
    }

    public void validateSubjectCodeUniquenessForUpdate(String code, Integer id) {
        if (subjectRepository.existsByCodeAndIdNot(code, id)) {
            throw new DuplicateResourceException("error.subject.duplicate_resource.code",
                    translatorService.getEntityTranslatedName(Subject.class), code);
        }
    }

    public void validateSubjectNameUniqueness(String name) {
        if (subjectRepository.existsByName(name)) {
            throw new DuplicateResourceException("error.subject.duplicate_resource.name",
                    translatorService.getEntityTranslatedName(Subject.class), name);
        }
    }

    public void validateSubjectNameUniquenessForUpdate(String name, Integer id) {
        if (subjectRepository.existsByNameAndIdNot(name, id)) {
            throw new DuplicateResourceException("error.subject.duplicate_resource.name",
                    translatorService.getEntityTranslatedName(Subject.class), name);
        }
    }

    public void validateSubjectForDeletionAndDeactivation(Integer id) {
        var subject = subjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("error.not_found",
                        translatorService.getEntityTranslatedName(Subject.class), id));

        if (subjectRepository.existsCourseForSubject(id)) {
            throw new SubjectDeletionConstraintException(
                    "error.subject.delete.has_courses_associated",
                    translatorService.getEntityTranslatedName(Subject.class), subject.getCode());
        }

        if (subjectRepository.isPrerequisiteForOtherSubjects(id)) {
            throw new SubjectDeactivatedException(
                    "error.subject.delete.has_prerequisites_associated",
                    translatorService.getEntityTranslatedName(Subject.class), subject.getCode());
        }
    }

    public void validatePrerequisites(List<Integer> ids) {
        var prerequisites = subjectRepository.findAllByIds(ids);

        if (prerequisites.size() != ids.size()) {
            var missingPrerequisites = prerequisites.stream()
                    .filter(subject -> !ids.contains(subject.getId()));

            throw new ResourceNotFoundException("error.subject.not_found_list",
                    missingPrerequisites);
        }

        var inactivePrerequisites = prerequisites.stream().filter(subject -> !subject.isActive())
                .toList();
        if (!inactivePrerequisites.isEmpty()) {
            throw new SubjectDeactivatedException("error.subject.update.deactivated_prerequisite",
                    inactivePrerequisites);
        }
    }
}
