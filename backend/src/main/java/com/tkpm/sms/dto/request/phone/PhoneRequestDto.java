package com.tkpm.sms.dto.request.phone;

import com.tkpm.sms.validator.required.RequiredConstraint;
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
    @Pattern(regexp = "^\\+?\\d+$", message = "INVALID_PHONE_NUMBER")
    String phoneNumber;
}
