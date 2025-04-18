package com.tkpm.sms.infrastructure.persistence.mapper;

import com.tkpm.sms.domain.model.Course;
import com.tkpm.sms.infrastructure.persistence.entity.CourseEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = {
        ProgramPersistenceMapper.class,
        SubjectPersistenceMapper.class
})
public interface CoursePersistenceMapper {
    CourseEntity toEntity(Course domain);

    Course toDomain(CourseEntity entity);
}
