package com.tkpm.sms.application.dto.request.phone;

import com.tkpm.sms.application.validator.required.RequiredConstraint;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PhoneRequestDto {
    String countryCode;

    @RequiredConstraint(field = "Phone number")
    @Pattern(regexp = "^(\\+\\d{1,3})?0?\\d{6,14}$", message = "INVALID_PHONE_NUMBER")
    String phoneNumber;
}
