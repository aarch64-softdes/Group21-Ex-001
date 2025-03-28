package com.tkpm.sms.dto.request.setting;

import com.tkpm.sms.validator.required.RequiredConstraint;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PhoneSettingRequestDto {
    @RequiredConstraint(field = "Supported Country codes")
    List<String> supportedCountryCodes;
}
