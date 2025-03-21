package com.tkpm.sms.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddressUpdateRequestDto {
    String street;
    String ward;
    String district;
    String province;
    String country;
}
