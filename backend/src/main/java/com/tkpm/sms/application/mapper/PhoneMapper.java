package com.tkpm.sms.application.mapper;

import com.tkpm.sms.application.dto.request.phone.PhoneRequestDto;
import com.tkpm.sms.application.dto.response.PhoneDto;
import com.tkpm.sms.domain.valueobject.Phone;

public interface PhoneMapper {
    PhoneDto toPhoneDto(String phone);
    PhoneDto toPhoneDto(Phone phone);
    PhoneRequestDto toPhoneRequestDto(String phone);
    String formatPhoneNumber(PhoneRequestDto phoneDto);
    Phone toPhone(String phoneString);
}
