package com.tkpm.sms.infrastructure.persistence.repository;

import com.tkpm.sms.domain.common.PageRequest;
import com.tkpm.sms.domain.common.PageResponse;
import com.tkpm.sms.domain.model.Status;
import com.tkpm.sms.domain.repository.StatusRepository;
import com.tkpm.sms.infrastructure.persistence.entity.StatusEntity;
import com.tkpm.sms.infrastructure.persistence.entity.TranslationEntity;
import com.tkpm.sms.infrastructure.persistence.jpa.StatusJpaRepository;
import com.tkpm.sms.infrastructure.persistence.jpa.TextContentJpaRepository;
import com.tkpm.sms.infrastructure.persistence.jpa.TranslationJpaRepository;
import com.tkpm.sms.infrastructure.persistence.mapper.StatusPersistenceMapper;
import com.tkpm.sms.infrastructure.utils.PagingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class StatusRepositoryImpl implements StatusRepository {
    private final StatusJpaRepository statusJpaRepository;
    private final TextContentJpaRepository textContentJpaRepository;
    private final TranslationJpaRepository translationJpaRepository;

    private final StatusPersistenceMapper statusPersistenceMapper;

    @Override
    public Status save(Status status) {
        StatusEntity entity = statusPersistenceMapper.toEntity(status);

        // // TODO: ALTERNATIVE: Find the reason why the CASCADE.ALL is not working (saving
        // textContent does not save translations)
        // // Save translations
        var textContent = entity.getName();
        if (textContent != null && textContent.getTranslations() != null) {
            textContent.getTranslations().forEach(te -> te.setTextContent(textContent));
        }

        // Save the status entity
        entity = statusJpaRepository.save(entity);

        return statusPersistenceMapper.toDomain(entity);
    }

    @Override
    public Optional<Status> findById(Integer id) {
        return statusJpaRepository.findById(id).map(statusPersistenceMapper::toDomain);
    }

    @Override
    public Optional<Status> findByName(String name) {
        var translation = translationJpaRepository.findByTextAndLanguageCode(name,
                LocaleContextHolder.getLocale().getLanguage());
        List<StatusEntity> statuses = statusJpaRepository.findAll();

        var statusWithOriginalName = statuses.stream().filter(status -> status.getName()
                .getTranslations().stream().anyMatch(TranslationEntity::isOriginal));

        statuses = statuses.stream()
                .filter(status -> status.getName().getTranslations().stream()
                        .anyMatch(translationEntity -> translationEntity.equals(translation)))
                .toList();

        if (statuses.isEmpty()) {
            return statusWithOriginalName.findFirst().map(statusPersistenceMapper::toDomain);
        } else {
            return statuses.stream().findFirst().map(statusPersistenceMapper::toDomain);
        }
    }

    @Override
    public boolean existsByName(String name) {
        return statusJpaRepository.existsByName(name,
                LocaleContextHolder.getLocale().getLanguage());
    }

    @Override
    public boolean existsByNameAndIdNot(String name, Integer id) {
        return statusJpaRepository.existsByNameAndIdNot(name,
                LocaleContextHolder.getLocale().getLanguage(), id);
    }

    @Override
    public boolean existsByFromStatusIdAndToStatusId(Integer fromStatusId, Integer toStatusId) {
        return statusJpaRepository.existsByFromStatusIdAndToStatusId(fromStatusId, toStatusId);
    }

    @Override
    public PageResponse<Status> findAll(PageRequest pageRequest) {
        // Convert domain PageRequest to Spring Pageable
        Pageable pageable = PagingUtils.toSpringPageable(pageRequest);

        // Execute query with Spring Pageable
        Page<StatusEntity> page = statusJpaRepository
                .findAll(LocaleContextHolder.getLocale().getLanguage(), pageable);

        // Convert Spring Page to domain PageResponse
        List<Status> content = page.getContent().stream().map(statusPersistenceMapper::toDomain)
                .collect(Collectors.toList());

        return PageResponse.of(content, page.getNumber() + 1, // Convert 0-based to 1-based
                page.getSize(), page.getTotalElements(), page.getTotalPages());
    }

    @Override
    public void delete(Status status) {
        var entity = statusPersistenceMapper.toEntity(status);
        statusJpaRepository.delete(entity);
    }
}