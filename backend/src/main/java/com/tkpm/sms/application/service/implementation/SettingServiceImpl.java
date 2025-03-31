package com.tkpm.sms.application.service.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tkpm.sms.application.dto.request.setting.EmailDomainSettingRequestDto;
import com.tkpm.sms.application.dto.request.setting.PhoneSettingRequestDto;
import com.tkpm.sms.application.service.interfaces.SettingService;
import com.tkpm.sms.domain.exception.ErrorCode;
import com.tkpm.sms.domain.exception.GenericDomainException;
import com.tkpm.sms.domain.exception.ResourceNotFoundException;
import com.tkpm.sms.domain.model.Setting;
import com.tkpm.sms.domain.repository.SettingRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class SettingServiceImpl implements SettingService {
    static String EMAIL_SETTING = "email";
    static String PHONE_NUMBER_SETTING = "phonenumber";
    static String AT_SIGN = "@";

    ObjectMapper objectMapper;
    SettingRepository settingRepository;

    @Override
    public Setting getPhoneSetting() {
        return settingRepository.findByName(PHONE_NUMBER_SETTING)
                .orElseThrow(() -> new ResourceNotFoundException("Phone setting not found"));
    }

    @Override
    public Setting getEmailSetting() {
        return settingRepository.findByName(EMAIL_SETTING)
                .orElseThrow(() -> new ResourceNotFoundException("Email setting not found"));
    }

    @Override
    @Transactional
    public Setting updateEmailSetting(EmailDomainSettingRequestDto settingRequestDto) {
        Setting setting = settingRepository.findByName(EMAIL_SETTING)
                .orElseThrow(() -> new ResourceNotFoundException("Email setting not found"));

        String domain = settingRequestDto.getDomain();
        if (!domain.contains(AT_SIGN)) {
            domain = AT_SIGN + domain;
        }
        setting.setDetails(domain);

        return settingRepository.save(setting);
    }

    @Override
    @Transactional
    public Setting updatePhoneSetting(PhoneSettingRequestDto phoneSettingRequestDto) {
        Setting setting = settingRepository.findByName(PHONE_NUMBER_SETTING)
                .orElseThrow(() -> new ResourceNotFoundException("Phone setting not found"));

        try {
            String details = objectMapper.writeValueAsString(phoneSettingRequestDto.getSupportedCountryCodes());
            setting.setDetails(details);
            return settingRepository.save(setting);
        } catch (JsonProcessingException e) {
            throw new GenericDomainException("Error processing phone setting", ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}