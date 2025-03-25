package com.tkpm.sms.mapper;
import com.tkpm.sms.dto.request.setting.SettingRequestDto;
import com.tkpm.sms.dto.response.SettingDto;
import com.tkpm.sms.entity.Setting;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SettingMapper {
    @Mapping(target = "details", ignore = true)
    Setting toSetting(SettingDto settingDto);

    SettingDto toSettingDto(Setting setting);

    void updateSetting(@MappingTarget Setting setting, SettingRequestDto settingRequestDto);

}
