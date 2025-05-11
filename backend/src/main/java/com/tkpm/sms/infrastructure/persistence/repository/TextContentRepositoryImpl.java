package com.tkpm.sms.infrastructure.persistence.repository;

import com.tkpm.sms.domain.repository.TextContentRepository;
import com.tkpm.sms.domain.valueobject.TextContent;
import com.tkpm.sms.infrastructure.persistence.entity.TranslationEntity;
import com.tkpm.sms.infrastructure.persistence.jpa.TextContentJpaRepository;
import com.tkpm.sms.infrastructure.persistence.mapper.TextContentPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TextContentRepositoryImpl implements TextContentRepository {
    private final TextContentJpaRepository textContentJpaRepository;
    private final TextContentPersistenceMapper textContentPersistenceMapper;

    @Override
    public TextContent save(TextContent textContent) {
        var entity = textContentPersistenceMapper.toEntity(textContent);

        if (entity.getTranslations() != null) {
            for (TranslationEntity translation : entity.getTranslations()) {
                translation.setTextContent(entity);
            }
        }

        entity = textContentJpaRepository.save(entity);

        return textContentPersistenceMapper.toDomain(entity);
    }

    @Override
    public Optional<TextContent> findById(Integer id) {
        return textContentJpaRepository.findById(id).map(textContentPersistenceMapper::toDomain);
    }
}
