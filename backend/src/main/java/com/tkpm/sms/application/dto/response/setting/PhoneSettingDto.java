package com.tkpm.sms.application.dto.response.setting;

import com.tkpm.sms.domain.enums.SettingType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PhoneSettingDto extends SettingDto {
    List<String> supportedCountryCodes;

    public PhoneSettingDto(List<String> supportedCountryCodes) {
        super(SettingType.PHONE_NUMBER.getValue());
        this.supportedCountryCodes = supportedCountryCodes;
    }
}
