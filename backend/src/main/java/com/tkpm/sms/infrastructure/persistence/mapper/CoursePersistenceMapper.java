package com.tkpm.sms.infrastructure.persistence.mapper;

import com.tkpm.sms.application.mapper.ScheduleMapper;
import com.tkpm.sms.domain.model.Course;
import com.tkpm.sms.infrastructure.persistence.entity.CourseEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {ProgramPersistenceMapper.class,
        SubjectPersistenceMapper.class, ScheduleMapper.class})
public interface CoursePersistenceMapper {
    CourseEntity toEntity(Course domain);

    Course toDomain(CourseEntity entity);
}
