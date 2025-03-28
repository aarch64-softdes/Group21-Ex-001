package com.tkpm.sms.dto.request.setting;

import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class EmailDomainSettingRequestDto {
    String domain;
}
