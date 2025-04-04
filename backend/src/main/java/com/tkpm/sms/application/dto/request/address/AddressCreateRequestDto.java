package com.tkpm.sms.application.dto.request.address;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddressCreateRequestDto {
    String street;
    String ward;
    String district;
    String province;
    String country;
}
