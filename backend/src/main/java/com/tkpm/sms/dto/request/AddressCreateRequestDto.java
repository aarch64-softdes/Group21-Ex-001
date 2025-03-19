package com.tkpm.sms.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddressCreateRequestDto {
    String street;
    String district;
    String country;
    String ward;
}
