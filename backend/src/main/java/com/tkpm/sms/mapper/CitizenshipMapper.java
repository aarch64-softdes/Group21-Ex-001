package com.tkpm.sms.mapper;
import org.mapstruct.Mapper;

import com.tkpm.sms.entity.Citizenship;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CitizenshipMapper {
    Citizenship createCitizenship(String countryName);
    void updateCitizenship(@MappingTarget Citizenship citizenship, String countryName);
}
