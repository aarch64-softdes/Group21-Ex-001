package com.tkpm.sms.domain.service.validators;

import com.tkpm.sms.domain.exception.DuplicateResourceException;
import com.tkpm.sms.domain.exception.ResourceNotFoundException;
import com.tkpm.sms.domain.exception.SubjectDeactivatedException;
import com.tkpm.sms.domain.exception.SubjectDeletionConstraintException;
import com.tkpm.sms.domain.model.Subject;
import com.tkpm.sms.domain.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public void validateSubjectForDeletion(Integer id) {
        var subject = subjectRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(
                        String.format("Subject with id %s not found", id)
                )
        );

        if (subjectRepository.existsCourseForSubject(id)) {
            throw new SubjectDeletionConstraintException(
                    String.format("Subject with name %s has courses associated with it", subject.getName())
            );
        }

        if (subjectRepository.isPrerequisiteForOtherSubjects(id)) {
            throw new SubjectDeletionConstraintException(
                   String.format( "Subject with name %s is a prerequisite for other subjects", subject.getName())
            );
        }
    }

    public void validatePrerequisites(List<Integer> ids) {
        var prerequisites = subjectRepository.findAllByIds(ids);

        if (prerequisites.size() != ids.size()) {
            var missingPrerequisites = ids.stream().filter(
                    id -> prerequisites.stream().noneMatch(subject -> subject.getId().equals(id))
            );

            throw new ResourceNotFoundException(
                    String.format("Subject with id %s does not exist", missingPrerequisites)
            );
        }

        var inactivePrerequisites = prerequisites.stream()
                .filter(subject -> !subject.isActive())
                .toList();
        if (!inactivePrerequisites.isEmpty()) {
            throw new SubjectDeactivatedException(
                    String.format("Some prerequisites with ids %s are not active", inactivePrerequisites.stream().map(Subject::getId).toList())
            );
        }
    }
}
