package com.tkpm.sms.application.dto.request.setting;

import com.tkpm.sms.application.annotation.RequiredConstraint;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class EmailDomainSettingRequestDto {
    @RequiredConstraint(field = "Email domain")
    String domain;
}
