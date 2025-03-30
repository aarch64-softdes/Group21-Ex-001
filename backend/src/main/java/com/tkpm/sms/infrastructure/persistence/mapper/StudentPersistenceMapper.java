package com.tkpm.sms.infrastructure.persistence.mapper;

import com.tkpm.sms.domain.model.Student;
import com.tkpm.sms.domain.enums.Gender;
import com.tkpm.sms.infrastructure.persistence.entity.StudentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = {
        FacultyPersistenceMapper.class,
        ProgramPersistenceMapper.class,
        StatusPersistenceMapper.class,
        AddressPersistenceMapper.class,
        IdentityPersistenceMapper.class
})
public interface StudentPersistenceMapper {

    @Mapping(target = "gender", source = "gender", qualifiedByName = "genderEnumToString")
    StudentEntity toEntity(Student domain);

    @Mapping(target = "gender", source = "gender", qualifiedByName = "genderStringToEnum")
    Student toDomain(StudentEntity entity);

    @Named("genderEnumToString")
    default String genderEnumToString(Gender gender) {
        return gender != null ? gender.getDisplayName() : null;
    }

    @Named("genderStringToEnum")
    default Gender genderStringToEnum(String gender) {
        return gender != null ? Gender.fromDisplayName(gender) : null;
    }
}