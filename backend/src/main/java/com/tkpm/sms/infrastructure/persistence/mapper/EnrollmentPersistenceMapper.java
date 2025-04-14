package com.tkpm.sms.infrastructure.persistence.mapper;

import com.tkpm.sms.domain.model.Enrollment;
import com.tkpm.sms.infrastructure.persistence.entity.EnrollmentEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {
        StudentPersistenceMapper.class,
        CoursePersistenceMapper.class,
})
public interface EnrollmentPersistenceMapper {
    EnrollmentEntity toEntity(Enrollment enrollment);

    Enrollment toDomain(EnrollmentEntity enrollmentEntity);
}
