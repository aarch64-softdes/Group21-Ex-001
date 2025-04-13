package com.tkpm.sms.domain.service.validators;

import com.tkpm.sms.domain.exception.DuplicateResourceException;
import com.tkpm.sms.domain.exception.SubjectDeletionPrerequisiteConstraintException;
import com.tkpm.sms.domain.model.Subject;
import com.tkpm.sms.domain.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubjectDomainValidator {
    private final SubjectRepository subjectRepository;

    public void validateSubjectIsPrerequisiteForOtherSubjects(Integer subjectId) {
        if (subjectRepository.isPrerequisiteForOtherSubjects(subjectId)) {
            throw new SubjectDeletionPrerequisiteConstraintException(
                    String.format("Subject with id %d is a prerequisite for other subjects", subjectId)
            );
        }
    }

    public void validateSubjectCodeUniqueness(String code) {
        if (subjectRepository.existsByCode(code)) {
            throw new DuplicateResourceException(
                    String.format("Subject with code %s already exists", code)
            );
        }
    }

    public void validateSubjectCodeUniquenessForUpdate(String code, Integer id) {
        if(subjectRepository.existsByCodeAndIdNot(code, id)) {
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
        if(subjectRepository.existsByNameAndIdNot(name, id)) {
            throw new DuplicateResourceException(
                    String.format("Subject with name %s already exists", name)
            );
        }
    }

}
