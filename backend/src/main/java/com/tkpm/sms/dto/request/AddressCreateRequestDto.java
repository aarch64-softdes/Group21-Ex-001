package com.tkpm.sms.dto.request;

public record AddressCreateRequestDto(
        String street,
        String district,
        String country,
        String ward
) {
}
