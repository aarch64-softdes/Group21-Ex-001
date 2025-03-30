package com.tkpm.sms.application.dto.response.setting;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmailDomainSettingDto extends SettingDto {
    String domain;

    @Builder
    public EmailDomainSettingDto(String settingName, String domain) {
        super(settingName);
        this.domain = domain;
    }
}