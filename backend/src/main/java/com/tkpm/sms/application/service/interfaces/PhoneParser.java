package com.tkpm.sms.application.service.interfaces;

import com.tkpm.sms.domain.valueobject.Phone;
import com.tkpm.sms.dto.response.PhoneDto;

public interface PhoneParser{
    String parsePhoneNumber(String phone, String countryCode);
    Phone parsePhoneToPhone(String phoneString);
    PhoneDto parsePhoneToPhoneDto(String phone);
}
