package com.tkpm.sms.application.dto.response.setting;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PhoneSettingDto extends SettingDto {
    List<String> supportedCountryCodes;
}
