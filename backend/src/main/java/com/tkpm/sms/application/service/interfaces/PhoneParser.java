package com.tkpm.sms.application.service.interfaces;

import com.tkpm.sms.application.dto.response.PhoneDto;
import com.tkpm.sms.domain.valueobject.Phone;

public interface PhoneParser{
    String parsePhoneNumber(String phone, String countryCode);
    Phone parsePhoneToPhone(String phoneString);
    PhoneDto parsePhoneToPhoneDto(String phone);
}
