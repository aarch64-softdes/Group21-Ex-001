package com.tkpm.sms.domain.service;

public interface DomainEntityNameTranslator {
    String getEntityTranslatedName(Class<?> entityClass);
}
