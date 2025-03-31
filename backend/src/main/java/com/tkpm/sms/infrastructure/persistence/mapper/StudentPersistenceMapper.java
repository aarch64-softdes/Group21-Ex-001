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
})
public abstract class StudentPersistenceMapper {
    @Autowired
    PhoneMapper phoneMapper;

    @Mapping(target = "gender", source = "gender", qualifiedByName = "genderEnumToString")
    @Mapping(target = "phone", expression = "java(domain.getPhone().toString())")
    public abstract StudentEntity toEntity(Student domain);

    @Mapping(target = "gender", source = "gender", qualifiedByName = "genderStringToEnum")
    @Mapping(target = "phone", expression = "java(phoneMapper.toPhone(entity.getPhone()))")
    @Mapping(target = "status", source = "status", qualifiedByName = "toMinimalDomain")
    public abstract Student toDomain(StudentEntity entity);

    @Named("genderEnumToString")
    protected String genderEnumToString(Gender gender) {
        return gender != null ? gender.getDisplayName() : null;
    }

    @Named("genderStringToEnum")
    protected Gender genderStringToEnum(String gender) {
        return gender != null ? Gender.fromDisplayName(gender) : null;
    }
}