package com.tkpm.sms.application.service.implementation;

import com.tkpm.sms.application.dto.request.common.BaseCollectionRequest;
import com.tkpm.sms.application.dto.request.status.StatusRequestDto;
import com.tkpm.sms.application.mapper.StatusMapper;
import com.tkpm.sms.application.service.interfaces.StatusService;
import com.tkpm.sms.domain.common.PageRequest;
import com.tkpm.sms.domain.common.PageResponse;
import com.tkpm.sms.domain.exception.ResourceNotFoundException;
import com.tkpm.sms.domain.model.Status;
import com.tkpm.sms.domain.valueobject.TextContent;
import com.tkpm.sms.domain.valueobject.Translation;
import com.tkpm.sms.domain.repository.StatusRepository;
import com.tkpm.sms.domain.service.validators.StatusDomainValidator;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;

@Slf4j
@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class StatusServiceImpl implements StatusService {
    StatusRepository statusRepository;
    StatusDomainValidator statusDomainValidator;
    StatusMapper statusMapper;

    @NonFinal
    @Value("${app.locale.default}")
    String DEFAULT_LANGUAGE;

    @Override
    public PageResponse<Status> getAllStatuses(BaseCollectionRequest search) {
        PageRequest pageRequest = PageRequest.from(search);

        return statusRepository.findAll(pageRequest);
    }

    @Override
    public Status getStatusById(Integer id) {
        return statusRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(
                String.format("Status with id %s not found", id)));
    }

    @Override
    public Status getStatusByName(String name) {
        // return statusRepository.findByName(name);
        return null;
    }

    @Override
    @Transactional
    public Status createStatus(StatusRequestDto statusRequestDto) {
        // Validate name uniqueness
        statusDomainValidator.validateNameUniqueness(statusRequestDto.getName());
        var languageCode = LocaleContextHolder.getLocale().getLanguage();

        var translation = Translation.builder().text(statusRequestDto.getName())
                .languageCode(languageCode).isOriginal(languageCode.equals(DEFAULT_LANGUAGE))
                .build();

        var textContent = TextContent.builder().createdAt(LocalDateTime.now())
                .translations(Collections.singletonList(translation)).build();

        // Map DTO to domain entity
        Status status = statusMapper.toStatus(statusRequestDto);
        status.setName(textContent);

        // Save and return
        return statusRepository.save(status);
    }

    @Override
    @Transactional
    public Status updateStatus(Integer id, StatusRequestDto statusRequestDto) {
        // Validate name uniqueness for update
        var languageCode = LocaleContextHolder.getLocale().getLanguage();

        statusDomainValidator.validateNameUniquenessForUpdate(statusRequestDto.getName(), id);

        // Find existing status
        Status status = statusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Status with id %s not found", id)));

        var translation = status.getName().getTranslations().stream().filter(
                translationEntity -> translationEntity.getLanguageCode().equals(languageCode))
                .findFirst();

        if (translation.isEmpty()) {
            var newTranslation = Translation.builder().text(statusRequestDto.getName())
                    .languageCode(languageCode).isOriginal(languageCode.equals(DEFAULT_LANGUAGE))
                    .build();

            // Add new translation
            status.getName().getTranslations().add(newTranslation);
        } else {
            translation.get().setText(statusRequestDto.getName());
        }

        // Save and return
        return statusRepository.save(status);
    }

    @Override
    @Transactional
    public void deleteStatus(Integer id) {
        Status status = statusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Status with id %s not found", id)));

        statusRepository.delete(status);
    }

    @Override
    public boolean isTransitionAllowed(Integer fromStatusId, Integer toStatusId) {
        statusDomainValidator.validateStatusTransition(fromStatusId, toStatusId);
        return true;
    }

    @Override
    public boolean isTransitionAllowed(Status fromStatus, Status toStatus) {
        return fromStatus.canTransitionTo(toStatus);
    }
}