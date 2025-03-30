package com.tkpm.sms.domain.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Address {
    String id;
    String street;
    String ward;
    String district;
    String province;
    String country;

    public String getFullAddress() {
        return String.format("%s, %s, %s, %s, %s",
                street == null ? "" : street,
                ward == null ? "" : ward,
                district == null ? "" : district,
                province == null ? "" : province,
                country == null ? "" : country
        );
    }
}