package com.tkpm.sms.infrastructure.persistence.mapper;

import com.tkpm.sms.application.mapper.PhoneMapper;
import com.tkpm.sms.domain.enums.Gender;
import com.tkpm.sms.domain.model.Student;
import com.tkpm.sms.infrastructure.persistence.entity.StudentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = {
        FacultyPersistenceMapper.class,
        ProgramPersistenceMapper.class,
        StatusPersistenceMapper.class,
        AddressPersistenceMapper.class,
        IdentityPersistenceMapper.class,
        PhoneMapper.class
})
public interface StudentPersistenceMapper {
    @Autowired
    PhoneMapper phoneMapper = null;

    @Mapping(target = "gender", source = "gender", qualifiedByName = "genderEnumToString")
    @Mapping(target = "phone", expression = "java(domain.getPhone().toString())")
    StudentEntity toEntity(Student domain);

    @Mapping(target = "gender", source = "gender", qualifiedByName = "genderStringToEnum")
    @Mapping(target = "phone", expression = "java(phoneMapper.toPhone(entity.getPhone()))")
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