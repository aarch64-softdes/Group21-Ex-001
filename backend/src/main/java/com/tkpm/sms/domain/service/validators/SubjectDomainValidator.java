package com.tkpm.sms.domain.service.validators;

import com.tkpm.sms.domain.exception.DuplicateResourceException;
import com.tkpm.sms.domain.exception.ResourceNotFoundException;
import com.tkpm.sms.domain.exception.SubjectDeactivatedException;
import com.tkpm.sms.domain.exception.SubjectDeletionConstraintException;
import com.tkpm.sms.domain.model.Subject;
import com.tkpm.sms.domain.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class SubjectDomainValidator {
    private final SubjectRepository subjectRepository;

    public void validateSubjectCodeUniqueness(String code) {
        if (subjectRepository.existsByCode(code)) {
            throw new DuplicateResourceException(
                    String.format("Subject with code %s already exists", code)
            );
        }
    }

    public void validateSubjectCodeUniquenessForUpdate(String code, Integer id) {
        if (subjectRepository.existsByCodeAndIdNot(code, id)) {
            throw new DuplicateResourceException(
                    String.format("Subject with code %s already exists", code)
            );
        }
    }

    public void validateSubjectNameUniqueness(String name) {
        if (subjectRepository.existsByName(name)) {
            throw new DuplicateResourceException(
                    String.format("Subject with name %s already exists", name)
            );
        }
    }

    public void validateSubjectNameUniquenessForUpdate(String name, Integer id) {
        if (subjectRepository.existsByNameAndIdNot(name, id)) {
            throw new DuplicateResourceException(
                    String.format("Subject with name %s already exists", name)
            );
        }
    }

    public void validateSubjectForDeletionAndDeactivation(Integer id) {
        var subject = subjectRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(
                        String.format("Subject with id %s not found", id)
                )
        );

        if (subjectRepository.existsCourseForSubject(id)) {
            throw new SubjectDeletionConstraintException(
                    String.format("Subject with code %s has courses associated with it", subject.getCode())
            );
        }

        if (subjectRepository.isPrerequisiteForOtherSubjects(id)) {
            throw new SubjectDeletionConstraintException(
                    String.format("Subject with code %s is a prerequisite for other subjects", subject.getCode())
            );
        }
    }

    public void validatePrerequisites(List<Integer> ids) {
        var prerequisites = subjectRepository.findAllByIds(ids);

        if (prerequisites.size() != ids.size()) {
            var missingPrerequisites = prerequisites.stream().filter(
                    subject -> !ids.contains(subject.getId())
            );

            throw new ResourceNotFoundException(
                    String.format("The following subjects are not exists: %s", missingPrerequisites.map(Subject::getCode).toList())
            );
        }

        var inactivePrerequisites = prerequisites.stream()
                .filter(subject -> !subject.isActive())
                .toList();
        if (!inactivePrerequisites.isEmpty()) {
            throw new SubjectDeactivatedException(
                    String.format("The following subjects are inactive: %s", inactivePrerequisites.stream().map(Subject::getCode).toList())
            );
        }
    }
}
