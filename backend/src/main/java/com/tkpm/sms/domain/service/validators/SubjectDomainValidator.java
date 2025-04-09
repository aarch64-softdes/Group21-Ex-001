package com.tkpm.sms.domain.service.validators;

import com.tkpm.sms.domain.exception.DuplicateResourceException;
import com.tkpm.sms.domain.model.Subject;
import com.tkpm.sms.domain.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
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
        Optional<Subject> existingSubject = subjectRepository.findById(id);
        if(existingSubject.isPresent() && !existingSubject.get().getCode().equals(code) ) {
            throw new DuplicateResourceException(
                    String.format("Subject with code %s already exists", code)
            );
        }
    }


}
