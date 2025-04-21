package com.tkpm.sms.infrastructure.mapper;

import com.tkpm.sms.application.dto.request.phone.PhoneRequestDto;
import com.tkpm.sms.application.dto.response.PhoneDto;
import com.tkpm.sms.application.mapper.PhoneMapper;
import com.tkpm.sms.domain.valueobject.Phone;
import com.tkpm.sms.infrastructure.service.PhoneParserImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PhoneMapperImpl implements PhoneMapper {
    private final PhoneParserImpl phoneParser;

    public PhoneDto toPhoneDto(String phone) {
        if (phone == null) {
            return null;
        }
        return phoneParser.parsePhoneToPhoneDto(phone);
    }

    public PhoneDto toPhoneDto(Phone phone) {
        if (phone == null) {
            return null;
        }
        return new PhoneDto(phone.getNumber(), phone.getCountryCode());
    }

    public PhoneRequestDto toPhoneRequestDto(String phone) {
        if (phone == null) {
            return null;
        }
        return phoneParser.parsePhoneToPhoneRequestDto(phone);
    }

    public String formatPhoneNumber(PhoneRequestDto phoneDto) {
        if (phoneDto == null) {
            return null;
        }
        return phoneParser.parsePhoneNumber(phoneDto.getPhoneNumber(), phoneDto.getCountryCode());
    }

    public Phone toPhone(String phoneString) {
        if (phoneString == null) {
            return null;
        }
        return phoneParser.parsePhoneToPhone(phoneString);
    }
}