package com.tkpm.sms.application.dto.response.setting;

import com.tkpm.sms.domain.enums.SettingType;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmailDomainSettingDto extends SettingDto {
    String domain;

    @Builder
    public EmailDomainSettingDto(String domain) {
        super(SettingType.EMAIL.getValue());
        this.domain = domain;
    }
}