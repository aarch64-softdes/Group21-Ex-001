package com.tkpm.sms.domain.repository;

import com.tkpm.sms.domain.valueobject.TextContent;

import java.util.Optional;

public interface TextContentRepository {
    TextContent save(TextContent textContent);

    Optional<TextContent> findById(Integer id);
}