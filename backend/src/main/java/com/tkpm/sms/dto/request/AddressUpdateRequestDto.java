package com.tkpm.sms.dto.request;

public record AddressUpdateRequestDto(
        String street,
        String district,
        String country,
        String ward
) {}
