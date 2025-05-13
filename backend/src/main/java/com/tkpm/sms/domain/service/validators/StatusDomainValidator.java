package com.tkpm.sms.domain.service.validators;

import com.tkpm.sms.domain.exception.DuplicateResourceException;
import com.tkpm.sms.domain.exception.ResourceNotFoundException;
import com.tkpm.sms.domain.exception.UnsupportedStatusTransitionException;
import com.tkpm.sms.domain.model.Status;
import com.tkpm.sms.domain.repository.StatusRepository;
import com.tkpm.sms.domain.service.TranslatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;

@RequiredArgsConstructor
public class StatusDomainValidator {
    private final StatusRepository statusRepository;
    private final TranslatorService translatorService;

    public void validateNameUniqueness(String name) {
        if (statusRepository.existsByName(name)) {
            throw new DuplicateResourceException("error.status.duplicate_resource.name",
                    translatorService.getEntityTranslatedName(Status.class), name);
        }
    }

    public void validateNameUniquenessForUpdate(String name, Integer id) {
        if (statusRepository.existsByNameAndIdNot(name, id)) {
            throw new DuplicateResourceException("error.status.duplicate_resource.name",
                    translatorService.getEntityTranslatedName(Status.class), name);
        }
    }

    public void validateStatusTransition(Integer fromStatusId, Integer toStatusId) {
        if (!statusRepository.existsByFromStatusIdAndToStatusId(fromStatusId, toStatusId)) {
            var fromStatus = statusRepository.findById(fromStatusId)
                    .orElseThrow(() -> new ResourceNotFoundException("error.status.not_found",
                            translatorService.getEntityTranslatedName(Status.class), fromStatusId));

            var toStatus = statusRepository.findById(toStatusId)
                    .orElseThrow(() -> new ResourceNotFoundException("error.status.not_found",
                            translatorService.getEntityTranslatedName(Status.class), toStatusId));

            throw new UnsupportedStatusTransitionException(
                    "error.status.unsupported_status_transition",
                    fromStatus.getNameByLanguage(LocaleContextHolder.getLocale().getLanguage()),
                    toStatus.getNameByLanguage(LocaleContextHolder.getLocale().getLanguage()));
        }
    }
}